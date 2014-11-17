<?php
include("dbConfig.php");
$data = "";
$array = [];
if (!empty ($_POST["tableName"]) && !empty ($_POST["id"])) {
    switch ($_POST["tableName"]) {
        case "ap_unit":
            if (!empty ($_POST["Name"]) && !empty($_POST["Abbreviation"])) {
                $array = updateUnit($_POST["Name"], $_POST["Abbreviation"], $_POST["id"]);
            } else {
                $array["error"] = "Not all values are given: Name, Abbreviation";
            }
            break;
        case "ap_ingredient":
            if (!empty ($_POST["Name"]) && !empty($_POST["AllowedUnits"])) {
                $array = updateIngredient($_POST["Name"], $_POST["AllowedUnits"], $_POST["id"]);
            } else {
                $array["error"] = "Not all values are given: Name, AllowedUnits";
            }
            break;
        case "ap_recept_category":
            if (!empty ($_POST["Name"]) && !empty($_POST["Picture"])) {
                $array = updateCategory($_POST["Picture"], $_POST["Name"], $id);
            } else {
                $array["error"] = "Not all values are given: Name, Picture";
            }
            break;
        case "ap_recipe":
            if (!empty ($_POST["Name"]) && !empty($_POST["Author"]) && !empty($_POST["Duration"]) && !empty($_POST["Cost"]) && !empty($_POST["Persons"]) && !empty($_POST["Difficulty"]) && !empty($_POST["Picture"]) && !empty($_POST["Ingredients"]) && !empty($_POST["RecipeText"]) && !empty($_POST["Category"])) {
                $array = updateRecipe($_POST["Name"], $_POST["Author"], $_POST["Duration"], $_POST["Cost"], $_POST["Persons"], $_POST["Difficulty"], $_POST["Picture"], $_POST["Ingredients"], $_POST["RecipeText"], $_POST["Category"], $_POST["id"]);
            } else {
                $array["error"] = "Not all values are given: Name, Author, Duration, Cost, Persons, Difficulty, Picture, Ingredients, RecipeText, Category";
            }
            break;
        case "ap_difficulty_recept":
            if (!empty ($_POST["Description"])) {
                $array = updateDifficulty($_POST["Description"], $id);
            } else {
                $array["error"] = "Not all values are given: Description";
            }
            break;
        default:
            $array["error"] = "Tablename incorrect.";
            break;
    }
} else {
    $array["error"] = "Tablename of id not found.";
}
$data = json_encode($array);

function updateIngredient($name, $allowedUnits, $id)
{
    include("dbConfig.php");
    $data = [];
    $con = new mysqli($dbServer, $dbUsername, $dbPassword, $dbDatabase);
    if (mysqli_connect_errno()) {
        $data["error"] = "Failed to connect to MySQL: " . mysqli_connect_error();
    } else {
        $sql = "UPDATE `ap_ingredient` SET `Name` = ?, `AllowedUnits` = ? WHERE `ID` = ?";
        $query = $con->prepare($sql);
        if ($query === false) {
            $data["error"] = "Failed to prepare the query: " . $con->error;
        } else {
            $bp = $query->bind_param("ssi", $name, $allowedUnits, $id);
            if ($bp === false) {
                $data["error"] = "Failed to bind params: " . $query->error;
            } else {
                $exec = $query->execute();
                if ($exec === false) {
                    $data["error"] = "Failed to execute the query: " . $query->error;
                } else {
                    if ($query->affected_rows === 1) {
                        $data["succeeded"] = "Query successfully executed";
                    } else {
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

function updateCategory($picture, $name, $id)
{
    include("dbConfig.php");
    $data = [];
    $con = new mysqli($dbServer, $dbUsername, $dbPassword, $dbDatabase);
    if (mysqli_connect_errno()) {
        $data["error"] = "Failed to connect to MySQL: " . mysqli_connect_error();
    } else {
        $sql = "UPDATE `ap_recept_category` SET `Picture` = ?, `Name` = ? WHERE `ID` = ?";
        $query = $con->prepare($sql);
        if ($query === false) {
            $data["error"] = "Failed to prepare the query: " . $con->error;
        } else {
            $bp = $query->bind_param("ssi", $picture, $name, $id);
            if ($bp === false) {
                $data["error"] = "Failed to bind params: " . $query->error;
            } else {
                $exec = $query->execute();
                if ($exec === false) {
                    $data["error"] = "Failed to execute the query: " . $query->error;
                } else {
                    if ($query->affected_rows === 1) {
                        $data["succeeded"] = "Query successfully executed";
                    } else {
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

function updateDifficulty($description,$id)
{
    include("dbConfig.php");
    $data = [];
    $con = new mysqli($dbServer, $dbUsername, $dbPassword, $dbDatabase);
    if (mysqli_connect_errno()) {
        $data["error"] = "Failed to connect to MySQL: " . mysqli_connect_error();
    } else {
        $sql = "UPDATE `ap_difficulty_recept` SET `Description` = ? WHERE `ID` = ?";
        $query = $con->prepare($sql);
        if ($query === false) {
            $data["error"] = "Failed to prepare the query: " . $con->error;
        } else {
            $bp = $query->bind_param("si", $description, $id);
            if ($bp === false) {
                $data["error"] = "Failed to bind params: " . $query->error;
            } else {
                $exec = $query->execute();
                if ($exec === false) {
                    $data["error"] = "Failed to execute the query: " . $query->error;
                } else {
                    if ($query->affected_rows === 1) {
                        $data["succeeded"] = "Query successfully executed";
                    } else {
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

function updateRecipe($name, $author, $duration, $cost, $persons, $difficulty, $picture, $ingredients, $recipe, $category, $id)
{
    include("dbConfig.php");
    $data = [];
    $con = new mysqli($dbServer, $dbUsername, $dbPassword, $dbDatabase);
    if (mysqli_connect_errno()) {
        $data["error"] = "Failed to connect to MySQL: " . mysqli_connect_error();
    } else {
        $sql = "UPDATE `ap_recipe` SET `Recipename` = ?, `AuthorId` = ?, `Duration` = ?, `Cost` = ?, `NumberOfPersons` = ?, `DifficultyId` = ?, `Picture` = ?, `Ingredients` = ?, `RecipeText` = ?, `CategoryId` = ? WHERE `ID` = ?";
        $query = $con->prepare($sql);
        if ($query === false) {
            $data["error"] = "Failed to prepare the query: " . $con->error;
        } else {
            $bp = $query->bind_param("sissiisssii", $name, $author, $duration, $cost, $persons, $difficulty, $picture, $ingredients, $recipe, $category, $id);
            if ($bp === false) {
                $data["error"] = "Failed to bind params: " . $query->error;
            } else {
                $exec = $query->execute();
                if ($exec === false) {
                    $data["error"] = "Failed to execute the query: " . $query->error;
                } else {
                    if ($query->affected_rows === 1) {
                        $data["succeeded"] = "Query successfully executed";
                    } else {
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

function updateUnit($name, $abbr, $id)
{
    include("dbConfig.php");
    $data = [];
    $con = new mysqli($dbServer, $dbUsername, $dbPassword, $dbDatabase);
    if (mysqli_connect_errno()) {
        $data["error"] = "Failed to connect to MySQL: " . mysqli_connect_error();
    } else {
        $sql = "UPDATE `ap_unit` SET `Name` = ?, `Abbreviation` = ? WHERE `ID` = ?";
        $query = $con->prepare($sql);
        if ($query === false) {
            $data["error"] = "Failed to prepare the query: " . $con->error;
        } else {
            $bp = $query->bind_param("ssi", $name, $abbr, $id);
            if ($bp === false) {
                $data["error"] = "Failed to bind params: " . $query->error;
            } else {
                $exec = $query->execute();
                if ($exec === false) {
                    $data["error"] = "Failed to execute the query: " . $query->error;
                } else {
                    if ($query->affected_rows === 1) {
                        $data["succeeded"] = "Query successfully executed";
                    } else {
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
    <title>Update data</title>
</head>
<body>
<pre id="json"></pre>
<script type="text/javascript">
    document.getElementById('json').innerHTML = JSON.stringify(<?php print $data; ?>, null, 4);
</script>
</body>
</html>