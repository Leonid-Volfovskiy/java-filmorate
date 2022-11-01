package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MpaDbDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getById(int id) {
        String qs = "SELECT * FROM MPA WHERE RAITING_ID = ?";
        return jdbcTemplate.queryForObject(qs, this::prepareMpaFromBd, id);
    }

    @Override
    public List<Mpa> getAllMpas() {
        String qs = "SELECT * FROM MPA";
        return jdbcTemplate.query(qs, this::prepareMpaFromBd);
    }

    private Mpa prepareMpaFromBd(ResultSet rs, int rowNum) throws SQLException {
        int mpaId = rs.getInt("RAITING_ID");
        String mpaName = rs.getString("MPA");
        return new Mpa(mpaId, mpaName);
    }
}
