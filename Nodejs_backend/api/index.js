const express = require('express');
const axios = require('axios');

const router = express.Router();


router.get('/', (req, res) => {
  res.send("yes");
 // res.send({ data: [] });
});



router.get('/guardian', (req, res) => {
  
  axios.get("https://content.guardianapis.com/search?api-key=4f162d18-4259-459b-a536-39cc5475173d&section=(sport|business|technology|politics)&show-blocks=all")
  //.then((response) => {res.json(response.data)})
  //.then(response => {res.send(response.data.response.results)})
  .then(response => {res.send(response.data.response)})
  //.catch("error " + console.error)
  //res.send(response)
  //console.log(res)
  //res.send({ data: [] });
});

router.get('/politics', (req, res) => {
  
  axios.get("https://content.guardianapis.com/politics?api-key=4f162d18-4259-459b-a536-39cc5475173d&show-blocks=all")
  //.then((response) => {res.json(response.data)})
  //.then(response => {res.send(response.data.response.results)})
  .then(response => {res.send(response.data.response)})
  //.catch("error " + console.error)
  //res.send(response)
  //console.log(res)
  //res.send({ data: [] });
});

router.get('/sports', (req, res) => {
  
  axios.get("https://content.guardianapis.com/sport?api-key=4f162d18-4259-459b-a536-39cc5475173d&show-blocks=all")
  //.then((response) => {res.json(response.data)})
  //.then(response => {res.send(response.data.response.results)})
  .then(response => {res.send(response.data.response)})
  //.catch("error " + console.error)
  //res.send(response)
  //console.log(res)
  //res.send({ data: [] });
});

router.get('/world', (req, res) => {
  
  axios.get("https://content.guardianapis.com/world?api-key=4f162d18-4259-459b-a536-39cc5475173d&show-blocks=all")
  //.then((response) => {res.json(response.data)})
  //.then(response => {res.send(response.data.response.results)})
  .then(response => {res.send(response.data.response)})
  //.catch("error " + console.error)
  //res.send(response)
  //console.log(res)
  //res.send({ data: [] });
});

router.get('/business', (req, res) => {
  
  axios.get("https://content.guardianapis.com/business?api-key=4f162d18-4259-459b-a536-39cc5475173d&show-blocks=all")
  //.then((response) => {res.json(response.data)})
  //.then(response => {res.send(response.data.response.results)})
  .then(response => {res.send(response.data.response)})
  //.catch("error " + console.error)
  //res.send(response)
  //console.log(res)
  //res.send({ data: [] });
});
router.get('/technology', (req, res) => {
  
  axios.get("https://content.guardianapis.com/technology?api-key=4f162d18-4259-459b-a536-39cc5475173d&show-blocks=all")
  //.then((response) => {res.json(response.data)})
  //.then(response => {res.send(response.data.response.results)})
  .then(response => {res.send(response.data.response)})
  //.catch("error " + console.error)
  //res.send(response)
  //console.log(res)
  //res.send({ data: [] });
});
router.get('/search', (req, res) => {
  let param = req.query.foo
  let query="https://content.guardianapis.com/search?q=" + param +"&api-key=4f162d18-4259-459b-a536-39cc5475173d&show-blocks=all"
  console.log("param is " +param)
  console.log("query is " + query)
  axios.get(query)
  //.then((response) => {res.json(response.data)})
  //.then(response => {res.send(response.data.response.results)})
  .then(response => {res.send(response.data.response)})
  //.catch("error " + console.error)
  //res.send(response)
  //console.log(res)
  //res.send({ data: [] });
});

router.get('/science', (req, res) => {
  
  axios.get("https://content.guardianapis.com/science?api-key=4f162d18-4259-459b-a536-39cc5475173d&show-blocks=all")
  //.then((response) => {res.json(response.data)})
  //.then(response => {res.send(response.data.response.results)})
  .then(response => {res.send(response.data.response)})
  //.catch("error " + console.error)
  //res.send(response)
  //console.log(res)
  //res.send({ data: [] });
});
router.get('/home', (req, res) => {
  
  axios.get("https://content.guardianapis.com/search?order-by=newest&show-fields=starRating,headline,thumbnail,short-url&api-key=4f162d18-4259-459b-a536-39cc5475173d")
  //.then((response) => {res.json(response.data)})
  //.then(response => {res.send(response.data.response.results)})
  .then(response => {res.send(response.data.response)})
  //.catch("error " + console.error)
  //res.send(response)
  //console.log(res)
  //res.send({ data: [] });
});

router.get('/trends', (req, res) => {
  let param = req.query.word
  const googleTrends = require('google-trends-api');
  googleTrends.interestOverTime({keyword: param})
.then(function(results){
  console.log('These results are awesome', results);
})
.catch(function(err){
  console.error('Oh no there was an error', err);
});
  
  
 /* let query="https://content.guardianapis.com/search?q=" + param +"&api-key=4f162d18-4259-459b-a536-39cc5475173d&show-blocks=all"
  ///let query="https://content.guardianapis.com/search?q=coronavirus&api-key=4f162d18-4259-459b-a536-39cc5475173d&show-blocks=all"
 console.log("param is " +param)
  console.log("query is " + query)
  axios.get(query)
  //.then((response) => {res.json(response.data)})
  //.then(response => {res.send(response.data.response.results)})
  .then(response => {res.send(response.data.response)})
  //.catch("error " + console.error)
  //res.send(response)
  //console.log(res)
  //res.send({ data: [] });*/
});



module.exports =router;
