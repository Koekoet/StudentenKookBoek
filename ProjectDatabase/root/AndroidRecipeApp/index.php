<?php
include("databaseFunctions.php");
if (isset ($_GET["function"]) && isset ($_GET["tableName"])) {

    if (isset ($_GET["id"])) {
       $data = $_GET["function"]($_GET["tableName"], $_GET["id"]);
    } else {
        $data = $_GET["function"]($_GET["tableName"]);
    }
}
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