<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.util.*"%>
<html>
<title>Web auto</title>
<link href="js/style.css" rel="stylesheet">
<script src="http://code.jquery.com/jquery-latest.min.js"
        type="text/javascript"></script>
<script type="text/javascript">
<%
HttpSession currentSession = request.getSession();
Object o = currentSession.getAttribute("login");
String currentlogin="Не определен";
if(o!=null)
	currentlogin= currentSession.getAttribute("login").toString();
%>


$(document).ready(function() {
	var sendInfo = null;
	
	$('#btnSearch').click(function() {
		
		   var name = $('#name').val();
	       var surname = $('#surname').val();
	       var patronymic = $('#patronymic').val();
	       var city = $('#city').val();
	       var model = $('#model').val();
	       sendInfo = {
	           name: name,
	           surname: surname,
	           patronymic: patronymic,
	           city: city,
	           model: model
	       };
	       
			currentRequest = $.ajax({
			type:'post',
			dataType:'json',
			url: 'SearchServlet',
			data: {
				dataquery : JSON.stringify(sendInfo)	
			}
				
			,
			 beforeSend:function() { 
				 if (name=="" && surname=="" 
						 && patronymic=="" && city==""
						 && model==""){
					 alert('Введите данные для запроса !');
					 return false;
				 }
			 },
			success: function( data) {
				
				 if(data==""){
					 alert("Данных по вашему запросу нет.");
					 return;
				 }			
				 $("#records_table").find("tr:gt(0)").remove();  
                 var trHTML = '';
                 
                //$.each(data, function(i, arr) { 
                	 for (var i = 0; i < data.length; i++){
						    var name=data[i].name;		
							var surname=data[i].surname;
	                 		var patronymic=data[i].patronymic;
	                 		var city=data[i].city;
	                 		var number=data[i].number;
	                 		var model=data[i].model;
	                 		trHTML += 
	                         	'<tr><td>' + name + 
	                         	'</td><td>' + surname + 
	                         	'</td><td>' + patronymic + 
	                         	'</td><td>' + city + 
	                         	'</td><td>' + number + 
	                         	'</td><td>' + model  +
	                        	'</td></tr>';    
		                 		
		                
		           };
				 
				 $('#records_table').append(trHTML);
                  } 
		
		});
});
});
</script>
</head>
    
<body>
<div class="tg">
	<h2> Данные автовладельцев: </h2>
		<table>
		<tr>	
		<th valign="top">
		<table id="button_table">
	    	<tr><th align="center" width="120">Имя<input type="text" 		id="name"></th></tr>
	        <tr><th align="center" width="120">Фамилия<input type="text" 	id="surname"></th></tr>
	        <tr><th align="center" width="120">Отчество<input type="text" 	id="patronymic"></th></tr>
	        <tr><th align="center" width="120">Город <input type="text" 	id="city"></th></tr>
	        <tr><th align="center" width="120">Марка <input type="text" 	id="model"></th></tr>
		</table>
		<br>
		<input type="button" value="Поиск" id="btnSearch">
		<br>
		</th>
		
		<th valign="top">
		<table id="records_table">
    	<tr>
	        <th align="center" width="100" >Имя</th>
	        <th align="center" width="100" >Фамилия</th>
	        <th align="center" width="100" >Отчество</th>
	        <th align="center" width="100" >Город</th>
	        <th align="center" width="100" >Марка</th>
	        <th align="center" width="100" >Номер</th>
    	</tr>
		</table>
    	</th>
    	</tr>
    	</table>
</div>

 <p><span id="currentlogin">Вы залогинились - <%= currentlogin %></span> </p>
<form  method="post"  action="ExitServlet">
 <p><input type="submit" value="Выход" name="btnExit"> <p>
</form>


</body>
</html>