const { Pool } = require('pg');

const env = process.env;
const config = {
    host: env.DB_HOST,
    port: env.DB_PORT || '5432',
    user: env.DB_USER,
    password: env.DB_PASSWORD,
    database: env.DB_NAME,
}; // odkud se asi tak berou tyto údaje? ;)

const pool = new Pool(config);

async function query(query, params) {
    const {rows, fields} = await pool.query(query, params);
    return rows;
}

const express = require('express');
var app = express();

app.use(express.json());
app.use(express.urlencoded({ extended: false }));

const cors = require('cors');
app.use(cors({ origin: '*' }));

app.get('/tasks', async function(req, res, next) {
    const tasks = await query(
        'SELECT * FROM task ORDER BY createdOn ASC', // todo: vyber všechny úkoly seřazené od nejstarsiho
        []
    );
    res.json({"tasks": tasks ? tasks : []});
});

app.put('/tasks', async function(req, res, next) {
    const tasks = await query(
        'INSERT INTO task (name, createdOn) VALUES ($1, NOW()) RETURNING taskId, name, createdOn, completedOn;',
        [ req.body.name ]
    );
    res.json(tasks ? tasks[0] : {});
});

app.patch('/task/:taskId', async function(req, res) {
    await query(
        'UPDATE task SET completedOn = NOW() WHERE taskId = $1;', // todo: nastavit completedOn na aktuální čas pro daný úkol
        [ req.params.taskId ]
    );
    res.json({});
});

app.delete('/task/:taskId', async function(req, res) {
    await query(
        'DELETE FROM task WHERE taskId = $1;', // todo: smazat daný úkol
        [ req.params.taskId ]
    );
    res.json({});
});

app.listen(3001);

