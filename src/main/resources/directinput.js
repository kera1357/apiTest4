var serverUrl = 'http://localhost:8089';

function codeXML() {

 const input = document.getElementById("inputField").value;
 const endPointCases = serverUrl + '/integration/cases';
 const optional = {
  headers:{
   "Content-type": "text/xml; charset=UTF-8"
   },
   body:input,
   method:"POST"
 };

fetch(endPointCases, optional)
.then(data => {return data.json()})
.then(res => {console.log(res)})
.then(res => {console.log(res)})
.catch(error => console.log(log))


//const responseJson = JSON.parse(data.json);
//console.log(responseJson.resultLink);



}
