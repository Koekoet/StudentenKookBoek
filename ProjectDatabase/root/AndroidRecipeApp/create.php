<?php
include("dbConfig.php");
$data = "";
$array = [];
if (!empty ($_POST["tableName"])) {
    switch ($_POST["tableName"]) {
        case "ap_unit":
            if (!empty ($_POST["Name"]) && !empty($_POST["Abbreviation"])) {
                $array = createNewUnit($_POST["Name"], $_POST["Abbreviation"]);
            } else {
                $array["error"] = "Not all values are given: Name, Abbreviation";
            }
            break;
        case "ap_ingredient":
            if (!empty ($_POST["Name"]) && !empty($_POST["AllowedUnits"])) {
                $array = createNewIngredient($_POST["Name"], $_POST["AllowedUnits"]);
            } else {
                $array["error"] = "Not all values are given: Name, AllowedUnits";
            }
            break;
        case "ap_recept_category":
            if (!empty ($_POST["Name"]) && !empty($_POST["Picture"])) {
                $array = createNewCategory($_POST["Picture"], $_POST["Name"]);
            } else {
                $array["error"] = "Not all values are given: Name, Picture";
            }
            break;
        case "ap_recipe":
            if (!empty ($_POST["Name"]) && !empty($_POST["Author"]) && !empty($_POST["Duration"]) && !empty($_POST["Cost"]) && !empty($_POST["Persons"]) && !empty($_POST["Difficulty"]) && !empty($_POST["Picture"]) && !empty($_POST["Ingredients"]) && !empty($_POST["RecipeText"]) && !empty($_POST["Category"])) {
                $array = createNewRecipe($_POST["Name"], $_POST["Author"], $_POST["Duration"], $_POST["Cost"], $_POST["Persons"], $_POST["Difficulty"], $_POST["Picture"], $_POST["Ingredients"], $_POST["RecipeText"], $_POST["Category"]);
            } else {
                $array["error"] = "Not all values are given: Name, Author, Duration, Cost, Persons, Difficulty, Picture, Ingredients, RecipeText, Category";
            }
            break;
        case "ap_difficulty_recept":
            if (!empty ($_POST["Description"])) {
                $array = createNewDifficulty($_POST["Description"]);
            } else {
                $array["error"] = "Not all values are given: Description";
            }
            break;
        default:
            $array["error"] = "Tablename incorrect.";
            break;
    }
} else {
    $array["error"] = "Tablename not found.";
}
$data = json_encode($array);

function createNewIngredient($name, $allowedUnits)
{
    include("dbConfig.php");
    $data = [];
    $con = new mysqli($dbServer, $dbUsername, $dbPassword, $dbDatabase);
    if (mysqli_connect_errno()) {
        $data["error"] = "Failed to connect to MySQL: " . mysqli_connect_error();
    } else {
        $sql = "INSERT INTO `ap_ingredient` (`Name`,`AllowedUnits`) VALUES (?,?)";
        $query = $con->prepare($sql);
        if ($query === false) {
            $data["error"] = "Failed to prepare the query: " . $con->error;
        } else {
            $bp = $query->bind_param("ss", $name, $allowedUnits);
            if ($bp === false) {
                $data["error"] = "Failed to bind params: " . $query->error;
            } else {
                $exec = $query->execute();
                if ($exec === false) {
                    $data["error"] = "Failed to execute the query: " . $query->error;
                } else {
                    if($query->affected_rows === 1){
                        $data["succeeded"] = "Query successfully executed";
                    }else{
                        $data["error"] = "Something went wrong while executing query. Please try again.";
                    }
                }
            }
            $query->close();
        }
        $con->close();
    }
    return $data;
}

function createNewCategory($picture, $name)
{
    include("dbConfig.php");
    $data = [];
    $con = new mysqli($dbServer, $dbUsername, $dbPassword, $dbDatabase);
    if (mysqli_connect_errno()) {
        $data["error"] = "Failed to connect to MySQL: " . mysqli_connect_error();
    } else {
        $sql = "INSERT INTO `ap_recept_category` (`Picture`,`Name`) VALUES (?,?)";
        $query = $con->prepare($sql);
        if ($query === false) {
            $data["error"] = "Failed to prepare the query: " . $con->error;
        } else {
            $bp = $query->bind_param("ss", $picture, $name);
            if ($bp === false) {
                $data["error"] = "Failed to bind params: " . $query->error;
            } else {
                $exec = $query->execute();
                if ($exec === false) {
                    $data["error"] = "Failed to execute the query: " . $query->error;
                } else {
                    if($query->affected_rows === 1){
                        $data["succeeded"] = "Query successfully executed";
                    }else{
                        $data["error"] = "Something went wrong while executing query. Please try again.";
                    }
                }
            }
            $query->close();
        }
        $con->close();
    }
    return $data;
}

function createNewDifficulty($description)
{
    include("dbConfig.php");
    $data = [];
    $con = new mysqli($dbServer, $dbUsername, $dbPassword, $dbDatabase);
    if (mysqli_connect_errno()) {
        $data["error"] = "Failed to connect to MySQL: " . mysqli_connect_error();
    } else {
        $sql = "INSERT INTO `ap_difficulty_recept` (`Description`) VALUES (?)";
        $query = $con->prepare($sql);
        if ($query === false) {
            $data["error"] = "Failed to prepare the query: " . $con->error;
        } else {
            $bp = $query->bind_param("s", $description);
            if ($bp === false) {
                $data["error"] = "Failed to bind params: " . $query->error;
            } else {
                $exec = $query->execute();
                if ($exec === false) {
                    $data["error"] = "Failed to execute the query: " . $query->error;
                } else {
                    if($query->affected_rows === 1){
                        $data["succeeded"] = "Query successfully executed";
                    }else{
                        $data["error"] = "Something went wrong while executing query. Please try again.";
                    }
                }
            }
            $query->close();
        }
        $con->close();
    }
    return $data;
}

function createNewRecipe($name, $author,$duration,$cost,$persons,$difficulty,$picture,$ingredients,$recipe,$category)
{
    include("dbConfig.php");
    $data = [];
    $con = new mysqli($dbServer, $dbUsername, $dbPassword, $dbDatabase);
    if (mysqli_connect_errno()) {
        $data["error"] = "Failed to connect to MySQL: " . mysqli_connect_error();
    } else {
        $sql = "INSERT INTO `ap_recipe` (`Recipename`, `AuthorId`, `Duration`, `Cost`, `NumberOfPersons`, `DifficultyId`, `Picture`, `Ingredients`, `RecipeText`, `CategoryId`) VALUES (?,?,?,?,?,?,?,?,?,?)";
        $query = $con->prepare($sql);
        if ($query === false) {
            $data["error"] = "Failed to prepare the query: " . $con->error;
        } else {
            $bp = $query->bind_param("sissiisssi", $name,$author,$duration,$cost,$persons,$difficulty,$picture,$ingredients,$recipe,$category);
            if ($bp === false) {
                $data["error"] = "Failed to bind params: " . $query->error;
            } else {
                $exec = $query->execute();
                if ($exec === false) {
                    $data["error"] = "Failed to execute the query: " . $query->error;
                } else {
                    if($query->affected_rows === 1){
                        $data["succeeded"] = "Query successfully executed";
                    }else{
                        $data["error"] = "Something went wrong while executing query. Please try again.";
                    }
                }
            }
            $query->close();
        }
        $con->close();
    }
    return $data;
}

function createNewUnit($name, $abbr)
{
    include("dbConfig.php");
    $data = [];
    $con = new mysqli($dbServer, $dbUsername, $dbPassword, $dbDatabase);
    if (mysqli_connect_errno()) {
        $data["error"] = "Failed to connect to MySQL: " . mysqli_connect_error();
    } else {
        $sql = "INSERT INTO `ap_unit` (`Name`,`Abbreviation`) VALUES (?,?)";
        $query = $con->prepare($sql);
        if ($query === false) {
            $data["error"] = "Failed to prepare the query: " . $con->error;
        } else {
            $bp = $query->bind_param("ss", $name, $abbr);
            if ($bp === false) {
                $data["error"] = "Failed to bind params: " . $query->error;
            } else {
                $exec = $query->execute();
                if ($exec === false) {
                    $data["error"] = "Failed to execute the query: " . $query->error;
                } else {
                    if($query->affected_rows === 1){
                        $data["succeeded"] = "Query successfully executed";
                    }else{
                        $data["error"] = "Something went wrong while executing query. Please try again.";
                    }
                }
            }
            $query->close();
        }
        $con->close();
    }
    return $data;
}
?>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Insert data</title>
</head>
<body>
<pre id="json"></pre>
<script type="text/javascript">
    document.getElementById('json').innerHTML = JSON.stringify(<?php print $data; ?>, null, 4);
</script>
</body>
</html>