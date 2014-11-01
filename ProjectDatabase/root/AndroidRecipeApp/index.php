<?php
include("databaseFunctions.php");
$data = selectDataById("ap_unit",2);
?>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Document</title>
</head>
<body>
<pre id="json"></pre>
<?php
echo "<script type='text/javascript'>document.getElementById('json').innerHTML = JSON.stringify(" . $data . ",null,4);</script>";
?>
<script type="text/javascript"></script>
</body>
</html>