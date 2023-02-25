const express = require('express');

// Constants
const PORT = 8080;

// App
const app = express();
app.get('/', (req, res) => {
  res.send("안녕하세요");
});

app.listen(PORT);
console.log(`Running on http://localhost:${PORT}`);

