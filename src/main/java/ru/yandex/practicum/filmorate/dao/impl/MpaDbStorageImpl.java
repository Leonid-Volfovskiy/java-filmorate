package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@AllArgsConstructor
public class MpaDbStorageImpl implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getById(int id) {
        String qs = "SELECT * FROM MPA WHERE RAITING_ID = ?";
        return jdbcTemplate.queryForObject(qs, this::makeMpa, id);
    }

    @Override
    public List<Mpa> getAllMpas() {
        String qs = "SELECT * FROM MPA";
        return jdbcTemplate.query(qs, this::makeMpa);
    }

    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        int mpaId = rs.getInt("RAITING_ID");
        String mpaName = rs.getString("MPA");
        return new Mpa(mpaId, mpaName);
    }
}
