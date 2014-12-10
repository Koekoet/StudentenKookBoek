<?php
header('Content-Type: application/json');
$data = "";
$array = [];
if (!empty ($_POST["tableName"]) && !empty ($_POST["id"])) {
    if (is_numeric($_POST["id"])) {
        include("dbConfig.php");
        $teller = 0;
        $con = new mysqli($dbServer, $dbUsername, $dbPassword, $dbDatabase);
        if (mysqli_connect_errno()) {
            $array["error"] = "Failed to connect to MySQL: " . mysqli_connect_error();
        } else {
            $tableName = $_POST["tableName"];
            $id = $_POST["id"];
            $sql = "DELETE FROM $tableName WHERE `ID` = ?";
            $query = $con->prepare($sql);
            if ($query === false) {
                $array["error"] = "Failed to prepare the query: " . $con->error;
            } else {
                $bp = $query->bind_param("i", $id);
                if ($bp === false) {
                    $array["error"] = "Failed to bind params: " . $query->error;
                } else {
                    $exec = $query->execute();
                    if ($exec === false) {
                        $array["error"] = "Failed to execute the query: " . $query->error;
                    } else {
                        if ($query->affected_rows === 1) {
                            $array["succeeded"] = "Row successfully deleted";
                        } else {
                            $array["error"] = "No rows affected. Try again. (Did you pass correct id?)";
                        }
                    }
                }
                $query->close();
            }
            $con->close();
        }
    } else {
        $array["error"] = "ID is not a number";
    }
} else {
    $array["error"] = "Tablename or id not found.";
}
print json_encode($array);
//$data = json_encode($array);
?>