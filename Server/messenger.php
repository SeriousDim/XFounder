<?php
    if (isset($_POST['command'])) $command = htmlentities($_POST['command']);
    if (isset($_GET['command'])) $command = htmlentities($_GET['command']);
    
    if (empty($command)){
        echo json_encode(array('success'=>'no data'));
    }
    else{
    	$link = mysqli_connect("link", "dbname", "dbpass")
        	or die("Could not connect : " . mysqli_connection_error());
    	mysqli_select_db($link,"tablename") or die("Could not select database");
        if ($command=="getMessages"){
            if (isset($_GET['projectID'])&&isset($_GET['time'])){
                $projectID = $_GET['projectID'];
                $time = $_GET['time'];
                $query = "SELECT sender_id, name, data, date FROM messenger JOIN workers ON(sender_id=id)WHERE project_id='$projectID' and date>$time ORDER BY m_id";
                $result = mysqli_query($link, $query) or die("Query failed : " . mysqli_error());
                $b['messages'] = array();
                while($row = mysqli_fetch_assoc($result)) {
                    $b['messages'][] = $row;
                }
                $b['time']=$_SERVER['REQUEST_TIME']."";
                $json = json_encode($b);
                echo $json;
            }
            else{
                echo json_encode(array('success'=>'no data'));
            }
        }
        else if ($command=="addMessage"){
            if (isset($_POST['projectID']) && isset($_POST['message']) && isset($_POST['workerID'])){
                $projectID = $_POST['projectID'];
                $message = $_POST['message'];
                $workerID = $_POST['workerID'];
                $time = $_SERVER['REQUEST_TIME'];
                $query = "INSERT INTO messenger(sender_id,data,project_id,date) VALUES ('$workerID','$message','$projectID','$time')";
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