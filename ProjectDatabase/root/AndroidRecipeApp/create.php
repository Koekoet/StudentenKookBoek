<?php
header('Content-Type: application/json');
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
            if (!empty ($_POST["Name"]) && !empty($_POST["Author"]) && !empty($_POST["Duration"]) && !empty($_POST["Cost"]) && !empty($_POST["Persons"]) && !empty($_POST["Difficulty"]) && isset ($_POST["Picture"]) && !empty($_POST["Ingredients"]) && !empty($_POST["RecipeText"])) {
                $array = createNewRecipe($_POST["Name"], $_POST["Author"], $_POST["Duration"], $_POST["Cost"], $_POST["Persons"], $_POST["Difficulty"], $_POST["Picture"], $_POST["Ingredients"], $_POST["RecipeText"]);
            } else {
                $array["error"] = "Not all values are given: Name, Author, Duration, Cost, Persons, Difficulty, Picture, Ingredients, RecipeText";
            }
            break;
        case "ap_difficulty_recept":
            if (!empty ($_POST["Description"])) {
                $array = createNewDifficulty($_POST["Description"]);
            } else {
                $array["error"] = "Not all values are given: Description";
            }
            break;
        case "ap_recipes_by_category":
            if(!empty ($_POST["CategoryId"]) && !empty ($_POST["RecipeIDs"])){
                $array = createNewRecipesByCategory($_POST["CategoryId"],$_POST["RecipeIDs"]);
            }else{
                $array["error"] = "Not all values are given: CategoryId, RecipeIDs";
            }
            break;
        default:
            $array["error"] = "Tablename incorrect.";
            break;
    }
} else {
    $array["error"] = "Tablename not found.";
}
print json_encode($array);

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

function createNewRecipe($name, $author,$duration,$cost,$persons,$difficulty,$picture,$ingredients,$recipe)
{
    include("dbConfig.php");
    $data = [];
    $con = new mysqli($dbServer, $dbUsername, $dbPassword, $dbDatabase);
    if (mysqli_connect_errno()) {
        $data["error"] = "Failed to connect to MySQL: " . mysqli_connect_error();
    } else {
        $sql = "INSERT INTO `ap_recipe` (`Recipename`, `AuthorId`, `Duration`, `Cost`, `NumberOfPersons`, `DifficultyId`, `Picture`, `Ingredients`, `RecipeText`) VALUES (?,?,?,?,?,?,?,?,?)";
        $query = $con->prepare($sql);
        if ($query === false) {
            $data["error"] = "Failed to prepare the query: " . $con->error;
        } else {
            $bp = $query->bind_param("sissiisss", $name,$author,$duration,$cost,$persons,$difficulty,$picture,$ingredients,$recipe);
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

function createNewRecipesByCategory($category, $recipeIDs)
{
    include("dbConfig.php");
    $data = [];
    $con = new mysqli($dbServer, $dbUsername, $dbPassword, $dbDatabase);
    if (mysqli_connect_errno()) {
        $data["error"] = "Failed to connect to MySQL: " . mysqli_connect_error();
    } else {
        $sql = "INSERT INTO `ap_recipes_by_category` (`CategoryId`,`RecipeIds`) VALUES (?,?)";
        $query = $con->prepare($sql);
        if ($query === false) {
            $data["error"] = "Failed to prepare the query: " . $con->error;
        } else {
            $bp = $query->bind_param("is", $category, $recipeIDs);
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