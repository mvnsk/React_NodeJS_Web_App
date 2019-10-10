const express = require('express');
const axios = require('axios');
const router = express.Router();

router.get('/home', (req, res) => {
  
    axios.get("https://api.nytimes.com/svc/topstories/v2/home.json?api-key=Cpezxlu53t9R8G4n9yUphGtBKjMmAyA6")
    //.then((response) => {res.json(response.data)})
    .then(response => {res.send(response.data)})
    
    .catch("error " + console.error)
    //res.send(x);
    //console.log(res)
    //res.send({ data: [] });
  });

  router.get('/world', (req, res) => {
  
    axios.get("https://api.nytimes.com/svc/topstories/v2/world.json?api-key=Cpezxlu53t9R8G4n9yUphGtBKjMmAyA6")
    //.then((response) => {res.json(response.data)})
    .then(response => {res.send(response.data)})
    
    .catch("error " + console.error)
    //res.send(x);
    //console.log(res)
    //res.send({ data: [] });
  });
  router.get('/politics', (req, res) => {
  
    axios.get("https://api.nytimes.com/svc/topstories/v2/politics.json?api-key=Cpezxlu53t9R8G4n9yUphGtBKjMmAyA6")
    //.then((response) => {res.json(response.data)})
    .then(response => {res.send(response.data)})
    
    .catch("error " + console.error)
    //res.send(x);
    //console.log(res)
    //res.send({ data: [] });
  });
  router.get('/business', (req, res) => {
  
    axios.get("https://api.nytimes.com/svc/topstories/v2/business.json?api-key=Cpezxlu53t9R8G4n9yUphGtBKjMmAyA6")
    //.then((response) => {res.json(response.data)})
    .then(response => {res.send(response.data)})
    
    .catch("error " + console.error)
    //res.send(x);
    //console.log(res)
    //res.send({ data: [] });
  });
  router.get('/technology', (req, res) => {
  
    axios.get("https://api.nytimes.com/svc/topstories/v2/technology.json?api-key=Cpezxlu53t9R8G4n9yUphGtBKjMmAyA6")
    //.then((response) => {res.json(response.data)})
    .then(response => {res.send(response.data)})
    
    .catch("error " + console.error)
    //res.send(x);
    //console.log(res)
    //res.send({ data: [] });
  });
  router.get('/sports', (req, res) => {
  
    axios.get("https://api.nytimes.com/svc/topstories/v2/sports.json?api-key=Cpezxlu53t9R8G4n9yUphGtBKjMmAyA6")
    //.then((response) => {res.json(response.data)})
    .then(response => {res.send(response.data)})
    
    .catch("error " + console.error)
    //res.send(x);
    //console.log(res)
    //res.send({ data: [] });
  });
  router.get('/search', (req, res) => {
    let param = req.query.foo
    let query="https://api.nytimes.com/svc/search/v2/articlesearch.json?q="+ param +"&api-key=Cpezxlu53t9R8G4n9yUphGtBKjMmAyA6"
    
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
  
  module.exports =router;
