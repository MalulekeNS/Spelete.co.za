<?php
// /api/index.php
header('Content-Type: application/json; charset=utf-8');
session_start();
require_once __DIR__ . 'db.php';

$action = $_GET['action'] ?? $_POST['action'] ?? '';

function ok($data = [], $code = 200){ http_response_code($code); echo json_encode(['ok'=>true,'data'=>$data]); exit; }
function err($msg='Error', $code=400){ http_response_code($code); echo json_encode(['ok'=>false,'error'=>$msg]); exit; }
function cart_init(){ if(!isset($_SESSION['cart'])) $_SESSION['cart'] = []; } // product_id => qty

try {
  switch ($action) {
    case 'get_categories': {
      $q = $pdo->query("SELECT id,name,slug FROM categories WHERE is_active=1 ORDER BY name");
      ok($q->fetchAll());
    }

    case 'get_products': {
      $category = trim($_GET['category'] ?? '');
      $search   = trim($_GET['q'] ?? '');
      $limit    = max(0, (int)($_GET['limit'] ?? 0));
      $sql = "SELECT DISTINCT p.* FROM products p
              LEFT JOIN product_categories pc ON pc.product_id=p.id
              LEFT JOIN categories c ON c.id=pc.category_id
              WHERE p.is_active=1";
      $params = [];
      if ($category !== '') { $sql .= " AND c.slug = ?"; $params[] = $category; }
      if ($search !== '')   { $sql .= " AND (p.name LIKE ? OR p.description LIKE ?)"; $params[] = "%$search%"; $params[] = "%$search%"; }
      $sql .= " ORDER BY p.created_at DESC";
      if ($limit > 0) { $sql .= " LIMIT ". (int)$limit; }
      $stmt = $pdo->prepare($sql);
      $stmt->execute($params);
      ok($stmt->fetchAll());
    }

    case 'get_product': {
      $slug = trim($_GET['slug'] ?? '');
      if (!$slug) err('Missing slug');
      $stmt = $pdo->prepare("SELECT * FROM products WHERE slug = ? AND is_active=1 LIMIT 1");
      $stmt->execute([$slug]);
      $p = $stmt->fetch();
      if (!$p) err('Product not found', 404);
      ok($p);
    }

    case 'subscribe': {
      $email = trim($_POST['email'] ?? '');
      if (!filter_var($email, FILTER_VALIDATE_EMAIL)) err('Invalid email');
      $stmt = $pdo->prepare("INSERT IGNORE INTO subscribers (email) VALUES (?)");
      $stmt->execute([$email]);
      ok(['message'=>'Subscribed']);
    }

    case 'contact': {
      $name = trim($_POST['name'] ?? '');
      $email = trim($_POST['email'] ?? '');
      $subject = trim($_POST['subject'] ?? '');
      $message = trim($_POST['message'] ?? '');
      if (!$name || !$subject || !$message || !filter_var($email, FILTER_VALIDATE_EMAIL)) {
        err('Please complete all fields with a valid email.');
      }
      $stmt = $pdo->prepare("INSERT INTO contacts (name,email,subject,message) VALUES (?,?,?,?)");
      $stmt->execute([$name,$email,$subject,$message]);
      ok(['message'=>'Message received']);
    }

    case 'cart_add': {
      cart_init();
      $pid = (int)($_POST['product_id'] ?? 0);
      $qty = max(1, (int)($_POST['qty'] ?? 1));
      if ($pid <= 0) err('Invalid product');
      $_SESSION['cart'][$pid] = ($_SESSION['cart'][$pid] ?? 0) + $qty;
      ok(['cart'=>$_SESSION['cart']]);
    }

    case 'cart_update': {
      cart_init();
      $pid = (int)($_POST['product_id'] ?? 0);
      $qty = max(0, (int)($_POST['qty'] ?? 0));
      if ($pid <= 0) err('Invalid product');
      if ($qty === 0) unset($_SESSION['cart'][$pid]); else $_SESSION['cart'][$pid] = $qty;
      ok(['cart'=>$_SESSION['cart']]);
    }

    case 'cart_get': {
      cart_init();
      if (!$_SESSION['cart']) ok(['items'=>[], 'subtotal'=>0.00]);
      $ids = array_keys($_SESSION['cart']);
      $placeholders = implode(',', array_fill(0, count($ids), '?'));
      $stmt = $pdo->prepare("SELECT id,name,price,image_url FROM products WHERE id IN ($placeholders)");
      $stmt->execute($ids);
      $items = []; $subtotal = 0.00;
      foreach ($stmt->fetchAll() as $p) {
        $qty = (int)$_SESSION['cart'][$p['id']];
        $line = $qty * (float)$p['price'];
        $subtotal += $line;
        $items[] = [
          'id'=>(int)$p['id'],'name'=>$p['name'],'price'=>(float)$p['price'],
          'qty'=>$qty,'line_total'=>round($line,2),'image_url'=>$p['image_url']
        ];
      }
      ok(['items'=>$items,'subtotal'=>round($subtotal,2)]);
    }

    case 'checkout': {
      cart_init();
      if (!$_SESSION['cart']) err('Cart is empty');
      $email = trim($_POST['email'] ?? '');
      if ($email && !filter_var($email, FILTER_VALIDATE_EMAIL)) err('Invalid email');

      $ids = array_keys($_SESSION['cart']);
      $placeholders = implode(',', array_fill(0, count($ids), '?'));
      $stmt = $pdo->prepare("SELECT id,name,price,stock FROM products WHERE id IN ($placeholders) AND is_active=1");
      $stmt->execute($ids);
      $found = $stmt->fetchAll();
      if (count($found) !== count($ids)) err('One or more products are unavailable');

      $subtotal = 0.00;
      foreach ($found as $p) {
        $qty = (int)$_SESSION['cart'][$p['id']];
        if ($qty > (int)$p['stock']) err("Insufficient stock for {$p['name']}");
        $subtotal += $qty * (float)$p['price'];
      }

      $pdo->beginTransaction();
      $ins = $pdo->prepare("INSERT INTO orders (user_id, guest_email, subtotal, status) VALUES (NULL, ?, ?, 'pending')");
      $ins->execute([$email ?: null, round($subtotal,2)]);
      $orderId = (int)$pdo->lastInsertId();

      $insItem = $pdo->prepare("INSERT INTO order_items (order_id, product_id, name, price, qty) VALUES (?,?,?,?,?)");
      $updStock = $pdo->prepare("UPDATE products SET stock = stock - ? WHERE id = ?");

      foreach ($found as $p) {
        $qty = (int)$_SESSION['cart'][$p['id']];
        $insItem->execute([$orderId, $p['id'], $p['name'], $p['price'], $qty]);
        $updStock->execute([$qty, $p['id']]);
      }
      $pdo->commit();

      $_SESSION['cart'] = [];
      ok(['order_id'=>$orderId,'subtotal'=>round($subtotal,2),'status'=>'pending']);
    }

    default:
      err('Unknown or missing action', 404);
  }
} catch (Throwable $e) {
  err('Server error', 500);
}
