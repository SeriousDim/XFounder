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
        if ($command=="getAllProjects"){
                $query = "SELECT title, p_id, name FROM workers JOIN projects ON (id=founder_id)";
                $result = mysqli_query($link, $query) or die("Query failed : " . mysqli_error());
                $b = array();
                while($row = mysqli_fetch_assoc($result)) {
                    $b[] = $row;
                }
                $json = json_encode($b);
                echo $json;
            
        } else if ($command=="getMyProjects"){
            if (isset($_GET['user_id'])){
                $user_id = $_GET['user_id'];
                $query = "SELECT title, p_id, name FROM jobs JOIN projects ON (p_id=project_id) JOIN workers ON(id = founder_id) WHERE user_id = $user_id AND NOT is_request";
                $result = mysqli_query($link, $query) or die("Query failed : " . mysqli_error());
                $b['projects'] = array();
                while($row = mysqli_fetch_assoc($result)) {
                    $b['projects'][] = $row;
                }
                $query2 = "SELECT name, job FROM workers WHERE id = $user_id";
                $result2 = mysqli_query($link, $query2) or die("Query failed : " . mysqli_error());
                $row2 = mysqli_fetch_assoc($result2);
                $b['name']=$row2['name'];
                $b['job']=$row2['job'];
                $json = json_encode($b);
                echo $json;
            }
            else{
                echo json_encode(array('success'=>'no data'));
            }
            
        }
        else if ($command=="addProject"){
            if (isset($_POST['userID']) && isset($_POST['title']) && isset($_POST['description'])){
                $userID = $_POST['userID'];
                $title = $_POST['title'];
                $description = $_POST['description'];
                $query = "INSERT projects(founder_id, title, description) VALUES ('$userID', '$title', '$description')";
                $result = mysqli_query($link, $query) or die("Query failed : " . mysqli_error());
                
                $id=mysqli_insert_id($link);
                
                $query2 = "INSERT INTO `jobs`(`user_id`, `project_id`, `can_manage`) VALUES ('$userID','$id',True)";
                $result2 = mysqli_query($link, $query2) or die("Query failed : " . mysqli_error());
                
                    
                if ($result2) {
                    $b=['success' => 'good', 'id'=>"$id"];
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
        else if ($command=="getRequests"){
            if (isset($_GET['projectID']) && isset($_GET['userID'])){
                $projectID = $_GET['projectID'];
                $userID = $_GET['userID'];
                
                $query = "SELECT name, job, id FROM jobs JOIN workers ON (id=user_id) WHERE project_id='$projectID' AND is_request";
                $result = mysqli_query($link, $query) or die("Query failed : " . mysqli_error());
                
                $b['workers'] = array();
                while($row = mysqli_fetch_assoc($result)) {
                    $b['workers'][] = $row;
                }
                
                $query2 = "SELECT can_manage FROM jobs WHERE project_id='$projectID' AND user_id = '$userID'";
                $result2 = mysqli_query($link, $query2) or die("Query failed : " . mysqli_error());
                $row2 = mysqli_fetch_assoc($result2);
                
                $b['can_manage'] = $row2['can_manage'];
                
                $json = json_encode($b);
                echo $json;
            }
            else{
                echo json_encode(array('success'=>'no data'));
            }
            
        }
        else if ($command=="getInfo"){
            if (isset($_GET['projectID'])){
                $projectID = $_GET['projectID'];
                
                $query = "SELECT name, job, id FROM jobs JOIN workers ON (id=user_id) WHERE project_id='$projectID' AND NOT is_request";
                $result = mysqli_query($link, $query) or die("Query failed : " . mysqli_error());
                
                $b['workers'] = array();
                while($row = mysqli_fetch_assoc($result)) {
                    $b['workers'][] = $row;
                }
                
                $query2 = "SELECT title, task_id, name, date_to from workers JOIN task ON (performer_id=id) WHERE project_id='$projectID'";
                $result2 = mysqli_query($link, $query2) or die("Query failed : " . mysqli_error());
                    
                $b['tasks'] = array();
                while($row2 = mysqli_fetch_assoc($result2)) {
                    $b['tasks'][] = $row2;
                }
                
                $query3 = "SELECT title, name FROM workers JOIN projects ON (id=founder_id) WHERE p_id='$projectID'";
                $result3 = mysqli_query($link, $query3) or die("Query failed : " . mysqli_error());
                $row3 = mysqli_fetch_assoc($result3);
                
                $b['title'] = $row3['title'];
                $b['founder'] = $row3['name'];
                
                $json = json_encode($b);
                echo $json;
            }
            else{
                echo json_encode(array('success'=>'no data'));
            }
            
        }
        else if ($command == 'getWorkers'){
            if (isset($_GET['projectID']) && isset($_GET['userID'])){
                $projectID = $_GET['projectID'];
                $userID = $_GET['userID'];
                
                $query = "SELECT can_manage FROM jobs WHERE project_id='$projectID' AND user_id='$userID'";
                $result = mysqli_query($link, $query) or die("Query failed : " . mysqli_error());
                $row = mysqli_fetch_assoc($result);
                if ($row['can_manage']){
                    $query = "SELECT name, id FROM jobs JOIN workers ON (id=user_id) WHERE project_id='$projectID'";
                    $result = mysqli_query($link, $query) or die("Query failed : " . mysqli_error());
                    
                    $b['workers'] = array();
                    while($row = mysqli_fetch_assoc($result)) {
                        $b['workers'][] = $row;
                    }
                }
                else{
                    $query = "SELECT name,id FROM workers WHERE id='$userID'";
                    $result = mysqli_query($link, $query) or die("Query failed : " . mysqli_error());
                    $b['workers'] = array();
                    
                    $row = mysqli_fetch_assoc($result);
                    $b['workers'][] = $row;
                    
                }
                $json = json_encode($b);
                echo $json;
                
            }
            else{
                echo json_encode(array('success'=>'no data'));
            }
            
        } else if ($command == 'getForeignProject'){
            if (isset($_GET['projectID'])){
                $projectID = $_GET['projectID'];
                
                $query = "SELECT description FROM projects WHERE p_id='$projectID'";
                $result = mysqli_query($link, $query) or die("Query failed : " . mysqli_error());
                $b = mysqli_fetch_assoc($result);
                $json = json_encode($b);
                echo $json;
                
            }
            else{
                echo json_encode(array('success'=>'no data'));
            }
            
        } else if ($command == 'addRequest'){
            if (isset($_GET['projectID']) && isset($_GET['userID'])){
                $projectID = $_GET['projectID'];
                $userID = $_GET['userID'];
                
                $query = "INSERT INTO `jobs`(`user_id`, `project_id`, `is_request`,`can_manage`) VALUES ('$userID','$projectID',True,False)";
                $result = mysqli_query($link, $query) or die("Query failed : " . mysqli_error());
                
                echo json_encode(array('success'=>'good'));
                
            }
            else{
                echo json_encode(array('success'=>'no data'));
            }
            
        } else if ($command == 'acceptRequest'){
            if (isset($_GET['projectID']) && isset($_GET['userID'])){
                $projectID = $_GET['projectID'];
                $userID = $_GET['userID'];
                
                $query = "UPDATE jobs SET is_request=0 WHERE user_id='$userID' AND project_id='$projectID'";
                $result = mysqli_query($link, $query) or die("Query failed : " . mysqli_error());
                $b = mysqli_fetch_assoc($result);
                echo json_encode(array('success'=>'good'));
                
            }
            else{
                echo json_encode(array('success'=>'no data'));
            }
            
        } else if ($command == 'deleteJob'){
            if (isset($_GET['projectID']) && isset($_GET['userID'])){
                $projectID = $_GET['projectID'];
                $userID = $_GET['userID'];
                
                $query = "DELETE FROM jobs WHERE user_id='$userID' AND project_id='$projectID'";
                $result = mysqli_query($link, $query) or die("Query failed : " . mysqli_error());
                $b = mysqli_fetch_assoc($result);
                echo json_encode(array('success'=>'good'));
            }
            else{
                echo json_encode(array('success'=>'no data'));
            }
            
        }

        mysqli_free_result($result);

        mysqli_close($link);
    }
?>