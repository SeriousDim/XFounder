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
        if ($command=="getList"){
            if (isset($_GET['user_id']) && isset($_GET['project_id'])){
                $user_id=$_GET['user_id'];
                $project_id=$_GET['project_id'];
                
                $query = "SELECT title, task_id, status, name, date_to from workers JOIN task ON (creator_id=id) WHERE performer_id='$user_id' AND project_id='$project_id'";
                $result = mysqli_query($link, $query) or die("Query failed : " . mysqli_error());
                $b['tasks'] = array();
                while($row = mysqli_fetch_assoc($result)) {
                    $b['tasks'][] = $row;
                }
                $json = json_encode($b);
                echo $json;
            }
            else{
                echo json_encode(array('success'=>'no data'));
            }
        }
        else if ($command=="getTask"){
            if (isset($_GET['taskID'])){
                $taskID = $_GET['taskID'];
                $query = "SELECT title, task.description,creator_id,performer_id, workers.name, status,date_from,date_to FROM workers JOIN task ON (performer_id=workers.id) WHERE task_id='$taskID'";
                $result = mysqli_query($link, $query) or die("Query failed : " . mysqli_error());
                if ($row = mysqli_fetch_assoc($result)) {
                    $b=$row;
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
        } else if ($command=="createTask"){
            if (isset($_POST['title']) && isset($_POST['description']) && isset($_POST['creator_id'])
             && isset($_POST['performer_id']) && isset($_POST['date_from']) && isset($_POST['date_to']) && isset($_POST['project_id'])){
                $title = $_POST['title'];
                $description = $_POST['description'];
                $creator_id = $_POST['creator_id'];
                $performer_id = $_POST['performer_id'];
                $date_from = $_POST['date_from'];
                $date_to = $_POST['date_to'];
                $project_id = $_POST['project_id'];
                $query = "INSERT INTO task(title, description,creator_id,performer_id,status,date_from,date_to,project_id) VALUES ('$title', '$description', '$creator_id','$performer_id','0','$date_from','$date_to','$project_id')";
                $result = mysqli_query($link, $query) or die("Query failed : ".mysqli_error());
                
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
        } else if ($command=='updateStatus'){
            if (isset($_POST['task_id']) && isset($_POST['status'])){
                $status = $_POST['status'];
                $task_id = $_POST['task_id'];
                $query = "UPDATE task SET status='$status' WHERE task_id=$task_id";
                $result = mysqli_query($link, $query) or die("Query failed : ".mysqli_error());
                
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
        } else if ($command=='updateTask'){
            if (isset($_POST['title']) && isset($_POST['description'])
             && isset($_POST['performer_id']) && isset($_POST['date_from']) && isset($_POST['date_to']) && isset($_POST['task_id'])){
                $title = $_POST['title'];
                $description = $_POST['description'];
                $performer_id = $_POST['performer_id'];
                $date_from = $_POST['date_from'];
                $date_to = $_POST['date_to'];
                $task_id = $_POST['task_id'];
                $query = "UPDATE task SET title='$title', description='$description', performer_id='$performer_id', date_from = '$date_from', date_to = '$date_to' WHERE task_id=$task_id";
                $result = mysqli_query($link, $query) or die("Query failed : ".mysqli_error());
                
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
        } else if ($command=="getGannt"){
            if (isset($_GET['user_id'])){
                $user_id = $_GET['user_id'];
                $query = "SELECT title, task_id, date_from, date_to from task WHERE performer_id='$user_id'";
                $result = mysqli_query($link, $query) or die("Query failed : " . mysqli_error());
                $b['tasks'] = array();
                while($row = mysqli_fetch_assoc($result)) {
                    $b['tasks'][] = $row;
                }
                $b['time'] = $_SERVER['REQUEST_TIME'];
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