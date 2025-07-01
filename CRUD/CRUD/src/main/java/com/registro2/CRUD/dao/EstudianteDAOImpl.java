package com.registro2.CRUD.dao;

import com.registro2.CRUD.model.Estudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EstudianteDAOImpl implements EstudianteDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Estudiante> findAll() {
        String sql = "SELECT * FROM estudiante";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Estudiante.class));
    }

    @Override
    public Estudiante findById(Long id) {
        String sql = "SELECT * FROM estudiante WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Estudiante.class), id);
    }

    @Override
    public void save(Estudiante estudiante) {
        String sql = "INSERT INTO estudiante (nombre, apellido, email, curso, codigo_estudiante) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, estudiante.getNombre(), estudiante.getApellido(), estudiante.getEmail(), estudiante.getCurso(), estudiante.getCodigoEstudiante());
    }

    @Override
    public void update(Estudiante estudiante) {
        String sql = "UPDATE estudiante SET nombre = ?, apellido = ?, email = ?, curso = ?, codigo_estudiante = ? WHERE id = ?";
        jdbcTemplate.update(sql, estudiante.getNombre(), estudiante.getApellido(), estudiante.getEmail(), estudiante.getCurso(), estudiante.getCodigoEstudiante(), estudiante.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM estudiante WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Estudiante> findAllPaged(int offset, int limit) {
        String sql = "SELECT * FROM estudiante LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Estudiante.class), limit, offset);
    }

    @Override
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM estudiante";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
} 