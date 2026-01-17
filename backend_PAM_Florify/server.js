const express = require('express');
const mysql = require('mysql2');
const bodyParser = require('body-parser');
const cors = require('cors');
const bcrypt = require('bcrypt');

const app = express();
app.use(cors());
app.use(bodyParser.json());

const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'Mysql2lila',
    database: 'db_florify',
    port: 3308
});

db.connect((err) => {
    if (err) return console.error('Gagal koneksi:', err.message);
    console.log('Terhubung ke Database MySQL!');

    // BUAT TABEL JIKA BELUM ADA
    const createUserTable = `CREATE TABLE IF NOT EXISTS user (
        user_id INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(255),
        email VARCHAR(255) UNIQUE,
        password VARCHAR(255)
    )`;

    const createTanamanTable = `CREATE TABLE IF NOT EXISTS tanaman (
        tanaman_id INT AUTO_INCREMENT PRIMARY KEY,
        user_id INT,
        nama VARCHAR(255),
        deskripsi TEXT,
        kategori_id INT,
        foto TEXT,
        FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
    )`;

    const createAktivitasTable = `CREATE TABLE IF NOT EXISTS aktivitas (
        aktivitas_id INT AUTO_INCREMENT PRIMARY KEY,
        tanaman_id INT,
        jenis_aktivitas VARCHAR(255),
        tanggal VARCHAR(255),
        catatan TEXT,
        FOREIGN KEY (tanaman_id) REFERENCES tanaman(tanaman_id) ON DELETE CASCADE
    )`;

    // BUAT TABEL SECARA BERURUTAN (User -> Tanaman -> Aktivitas)
    // DROP dulu untuk memastikan schema terbaru
    const dropAktivitas = `DROP TABLE IF EXISTS aktivitas`;

    db.query(createUserTable, (err) => {
        if (err) console.error('Error create user table:', err);
        else {
            db.query(createTanamanTable, (err) => {
                if (err) console.error('Error create tanaman table:', err);
                else {
                    db.query(dropAktivitas, (err) => {
                        if (err) console.error('Error drop aktivitas table:', err);
                        else {
                            db.query(createAktivitasTable, (err) => {
                                if (err) console.error('Error create aktivitas table:', err);
                                else console.log('Tabel Aktivitas berhasil diperbarui!');
                            });
                        }
                    });
                }
            });
        }
    });
});

// ==================== AUTHENTICATION ====================

// LOGIN
app.post('/login', async (req, res) => {
    const { email, password } = req.body;

    if (!email || !password) {
        return res.status(400).json({ error: 'Email dan password harus diisi' });
    }

    const sql = "SELECT user_id AS userId, username, email, password FROM user WHERE email = ?";

    db.query(sql, [email], async (err, results) => {
        if (err) {
            console.error('Login error:', err);
            return res.status(500).json({ error: err.message });
        }

        if (results.length > 0) {
            const user = results[0];

            // Cek apakah password sudah di-hash atau masih plain text
            let isValidPassword = false;
            if (user.password.startsWith('$2b$')) {
                // Password sudah di-hash, gunakan bcrypt
                isValidPassword = await bcrypt.compare(password, user.password);
            } else {
                // Password masih plain text (untuk backward compatibility)
                isValidPassword = (password === user.password);
            }

            if (isValidPassword) {
                console.log('Login successful:', user.email);
                // Jangan kirim password ke client
                delete user.password;
                res.json(user);
            } else {
                console.log('Login failed - wrong password for:', email);
                res.status(401).json({ error: 'Email atau password salah' });
            }
        } else {
            console.log('Login failed - email not found:', email);
            res.status(401).json({ error: 'Email atau password salah' });
        }
    });
});

// REGISTER
app.post('/register', async (req, res) => {
    const { username, email, password } = req.body;

    if (!username || !email || !password) {
        return res.status(400).json({ error: 'Semua field harus diisi' });
    }

    const checkSql = "SELECT * FROM user WHERE email = ?";
    db.query(checkSql, [email], async (err, results) => {
        if (err) {
            console.error('Register check error:', err);
            return res.status(500).json({ error: err.message });
        }

        if (results.length > 0) {
            return res.status(409).json({ error: 'Email sudah terdaftar' });
        }

        // Hash password sebelum disimpan
        const hashedPassword = await bcrypt.hash(password, 10);

        const insertSql = "INSERT INTO user (username, email, password) VALUES (?, ?, ?)";
        db.query(insertSql, [username, email, hashedPassword], (err, result) => {
            if (err) {
                console.error('Register insert error:', err);
                return res.status(500).json({ error: err.message });
            }

            console.log('Register successful:', email);
            res.json({
                status: 'success',
                message: 'Registrasi berhasil',
                userId: result.insertId
            });
        });
    });
});

// ==================== TANAMAN ====================

// GET Tanaman Berdasarkan UserId
app.get('/tanaman', (req, res) => {
    const userId = req.query.userId;

    if (!userId) {
        return res.status(400).json({ error: 'userId harus diisi' });
    }

    const sql = `SELECT 
        tanaman_id AS tanamanId, 
        nama, 
        deskripsi, 
        kategori_id AS kategoriId,
        '' AS kategoriNama,
        foto,
        user_id AS userId
    FROM tanaman 
    WHERE user_id = ?`;

    db.query(sql, [userId], (err, results) => {
        if (err) return res.status(500).json({ error: err.message });
        res.json(results);
    });
});

// GET DETAIL TANAMAN
app.get('/tanaman/:id', (req, res) => {
    const sql = `SELECT 
        tanaman_id AS tanamanId, 
        nama, 
        deskripsi, 
        kategori_id AS kategoriId,
        '' AS kategoriNama,
        foto,
        user_id AS userId
    FROM tanaman 
    WHERE tanaman_id = ?`;

    db.query(sql, [req.params.id], (err, results) => {
        if (err) return res.status(500).json({ error: err.message });

        if (results.length === 0) {
            return res.status(404).json({ error: 'Tanaman tidak ditemukan' });
        }

        res.json(results[0]);
    });
});

// INSERT TANAMAN
app.post('/tanaman', (req, res) => {
    const { nama, deskripsi, kategoriId, foto, userId } = req.body;

    if (!nama || !deskripsi || !kategoriId || !userId) {
        return res.status(400).json({ error: 'Nama, deskripsi, kategoriId, dan userId wajib diisi' });
    }

    const sql = "INSERT INTO tanaman (nama, deskripsi, kategori_id, foto, user_id) VALUES (?, ?, ?, ?, ?)";
    db.query(sql, [nama, deskripsi, kategoriId, foto || null, userId], (err, result) => {
        if (err) return res.status(500).json({ error: err.message });
        res.json({ status: 'success', tanamanId: result.insertId });
    });
});

// UPDATE Tanaman
app.put('/tanaman/:id', (req, res) => {
    const { nama, deskripsi, kategoriId, foto } = req.body;

    if (!nama || !deskripsi || !kategoriId) {
        return res.status(400).json({ error: 'Nama, deskripsi, dan kategoriId wajib diisi' });
    }

    const sql = "UPDATE tanaman SET nama=?, deskripsi=?, kategori_id=?, foto=? WHERE tanaman_id=?";
    db.query(sql, [nama, deskripsi, kategoriId, foto || null, req.params.id], (err, result) => {
        if (err) return res.status(500).json({ error: err.message });

        if (result.affectedRows === 0) {
            return res.status(404).json({ error: 'Tanaman tidak ditemukan' });
        }

        res.json({ status: 'success' });
    });
});

// DELETE TANAMAN
app.delete('/tanaman/:id', (req, res) => {
    db.query("DELETE FROM tanaman WHERE tanaman_id = ?", [req.params.id], (err, result) => {
        if (err) return res.status(500).json({ error: err.message });

        if (result.affectedRows === 0) {
            return res.status(404).json({ error: 'Tanaman tidak ditemukan' });
        }

        res.json({ status: 'success' });
    });
});

// ==================== AKTIVITAS ====================

// GET Aktivitas by Tanaman
app.get('/tanaman/:id/aktivitas', (req, res) => {
    const sql = `SELECT 
        aktivitas_id AS aktivitasId, 
        tanaman_id AS tanamanId, 
        jenis_aktivitas AS tipeAktivitas,
        tanggal AS tanggalAktivitas,
        catatan AS notes
    FROM aktivitas 
    WHERE tanaman_id = ? 
    ORDER BY tanggal DESC`;

    db.query(sql, [req.params.id], (err, results) => {
        if (err) return res.status(500).json({ error: err.message });
        res.json(results);
    });
});

// INSERT Aktivitas
app.post('/aktivitas', (req, res) => {
    const { tanamanId, tipeAktivitas, tanggalAktivitas, notes } = req.body;

    if (!tanamanId || !tipeAktivitas || !tanggalAktivitas) {
        return res.status(400).json({ error: 'tanamanId, tipeAktivitas, dan tanggalAktivitas wajib diisi' });
    }

    const sql = "INSERT INTO aktivitas (tanaman_id, jenis_aktivitas, tanggal, catatan) VALUES (?, ?, ?, ?)";
    db.query(sql, [tanamanId, tipeAktivitas, tanggalAktivitas, notes || ''], (err, result) => {
        if (err) return res.status(500).json({ error: err.message });
        res.json({ status: 'success', aktivitasId: result.insertId });
    });
});

// UPDATE Aktivitas
app.put('/aktivitas/:id', (req, res) => {
    const { tipeAktivitas, tanggalAktivitas, notes } = req.body;

    if (!tipeAktivitas || !tanggalAktivitas) {
        return res.status(400).json({ error: 'tipeAktivitas dan tanggalAktivitas wajib diisi' });
    }

    const sql = "UPDATE aktivitas SET jenis_aktivitas=?, tanggal=?, catatan=? WHERE aktivitas_id=?";
    db.query(sql, [tipeAktivitas, tanggalAktivitas, notes || '', req.params.id], (err, result) => {
        if (err) return res.status(500).json({ error: err.message });

        if (result.affectedRows === 0) {
            return res.status(404).json({ error: 'Aktivitas tidak ditemukan' });
        }

        res.json({ status: 'success' });
    });
});

// DELETE Aktivitas
app.delete('/aktivitas/:id', (req, res) => {
    db.query("DELETE FROM aktivitas WHERE aktivitas_id = ?", [req.params.id], (err, result) => {
        if (err) return res.status(500).json({ error: err.message });

        if (result.affectedRows === 0) {
            return res.status(404).json({ error: 'Aktivitas tidak ditemukan' });
        }

        res.json({ status: 'success' });
    });
});

app.listen(3000, () => console.log('Server running on port 3000'));