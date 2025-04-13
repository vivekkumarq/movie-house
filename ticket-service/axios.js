var axios = require('axios');
var data = JSON.stringify({
  "user": {
    "id": "9fe6896f-8d8c-4f11-8ec9-8d45c58e793b",
    "name": "Jeevan Kumar"
  },
  "status": "UPCOMING",
  "show": {
    "id": "edd2d6ec-276f-42b7-83f4-3d9b28cfd111"
  },
  "seats": [
    {
      "id": "95c9e13c-54d7-457e-afb4-a55c909478fd",
      "seatNumber": "A1",
      "seatType": "GOLD"
    },
    {
      "id": "4518deae-dba1-4df3-ac33-a5c4c8d6ec39",
      "seatNumber": "A2",
      "seatType": "GOLD"
    }
  ]
});

var config = {
  method: 'post',
  url: 'http://ws-21290.netcracker.com:8085/ticket-management/ticket',
  headers: {
    'Content-Type': 'application/json'
  },
  data : data
};

for(var i=0;i<5;i++){
axios(config)
.then(function (response) {
  console.log(JSON.stringify(response.data));
})
.catch(function (error) {
  console.log(error);
});
}
