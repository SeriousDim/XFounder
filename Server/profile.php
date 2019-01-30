<?php
    if (isset($_GET['command'])) $command = htmlentities($_GET['command']);
    if (isset($_POST['command'])) $command = htmlentities($_POST['command']);
    
    if (empty($command)){
        echo json_encode(array('success'=>'no data'));
    }
    else{
    	$link = mysqli_connect("link", "dbname", "dbpass")
        	or die("Could not connect : " . mysqli_connection_error());
    	mysqli_select_db($link,"tablename") or die("Could not select database");
        if ($command=="login"){
            if (isset($_GET['login']) && isset($_GET['password'])){
                $login=$_GET['login'];
                $password=$_GET['password'];
                
                $query = "SELECT id FROM workers WHERE login='$login' AND hash=MD5('$password')";
                $result = mysqli_query($link, $query) or die("Query failed : " . mysqli_error());
                if ($row = mysqli_fetch_assoc($result))
                    $b=["success"=>"good", "user_id"=>$row['id']];
                else
                    $b=["success"=>"bad"];
                $json = json_encode($b);
                echo $json;
            }
            else{
                echo json_encode(array('success'=>'no data'));
            }
        } else if ($command=="signUp"){
            if (isset($_POST['name']) && isset($_POST['login']) && isset($_POST['password'])
             && isset($_POST['job']) && isset($_POST['description'])){
                $name = $_POST['name'];
                $description = $_POST['description'];
                $login = $_POST['login'];
                $password = $_POST['password'];
                $job = $_POST['job'];
            
                $query = "SELECT id FROM workers WHERE login = '$login'";
                $result = mysqli_query($link, $query) or die("Query failed : ".mysqli_error());
                $row = mysqli_fetch_assoc($result);
                if (!$row) {
                    $query = "INSERT INTO workers(name, description, job, login, hash) VALUES ('$name','$description', '$job','$login', MD5('$password'))";
                    
                    $result = mysqli_query($link, $query) or die("Query failed : " . mysqli_error());
                    
                    $id=mysqli_insert_id($link);
                
                    $b=['success' => 'good', 'user_id'=>"$id"];
                }else{
                    $b=['success' => 'not unique'];
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