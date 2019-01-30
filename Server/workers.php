<?php
    if (isset($_GET['command'])) $command = htmlentities($_GET['command']);
    if (empty($command)){
        echo json_encode(array('success'=>'no data'));
    }
    else{
    	$link = mysqli_connect("link", "dbname", "dbpass")
        	or die("Could not connect : " . mysqli_connection_error());
    	mysqli_select_db($link,"tablename") or die("Could not select database");
        if ($command=="getName"){
            if (isset($_GET['WID'])){
                $WID = $_GET['WID'];
                $query = "SELECT name FROM workers WHERE id='$WID'";
                $result = mysqli_query($link, $query) or die("Query failed : " . mysqli_error());
                
                if ($row = mysqli_fetch_assoc($result)) {
                    $b=['success' => 'good', 'name'=>$row['name']];
                }
                else{
                    $b=['success' => 'bad'];
                }
                $json = json_encode($b);
                echo $json;
            }
            else{
                echo json_encode(array('success'=>'no data'));
            }
        }
        else if ($command=="getAll"){
            if (isset($_GET['WID'])){
                $WID = $_GET['WID'];
                $query = "SELECT name,description,job FROM workers WHERE id='$WID'";
                $result = mysqli_query($link, $query) or die("Query failed : " . mysqli_error());
                
                if ($row = mysqli_fetch_assoc($result)) {
                    $b=['success' => 'good', 'name'=>$row['name'], 'description'=>$row['description'],'job'=>$row['job']];
                }
                else{
                    $b=['success' => 'bad'];
                }
                $json = json_encode($b);
                echo $json;
            }
            else{
                echo json_encode(array('success'=>'no data'));
            }
        } else if ($command=='updateDescription'){
            if (isset($_GET['WID']) && isset($_GET['description'])){
                $WID = $_GET['WID'];
                $description = $_GET['description'];
                $query = "UPDATE workers SET description='$description' WHERE id='$WID'";
                $result = mysqli_query($link, $query) or die("Query failed : " . mysqli_error());
                
                if ($result) {
                    $b=['success' => 'good'];
                }
                else{
                    $b=['success' => 'bad'];
                }
                $json = json_encode($b);
                echo $json;
            }
            else{
                echo json_encode(array('success'=>'no data'));
            }
        }

        mysqli_free_result($result);

        mysqli_close($link);
    }
?>