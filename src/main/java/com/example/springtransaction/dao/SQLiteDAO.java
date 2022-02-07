package com.example.springtransaction.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component("sqliteDAO")
public class SQLiteDAO implements MP3Dao {

    private static final String mp3Table = "mp3";
    private static final String mp3View = "mp3_view";

    private SimpleJdbcInsert insertMP3;

    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertMP3 = new SimpleJdbcInsert(dataSource)
                .withTableName("mp3").usingColumns("name","author");
    }

    @Override
    public int insert(MP3 mp3) {
        String sqlInsertAuthor = "insert into author (name) VALUES (:authorName)";

        Author author = mp3.getAuthor();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("authorName", author.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sqlInsertAuthor, params, keyHolder);
        int author_id = keyHolder.getKey().intValue();

        // неверное имя поля author_id2
        String sqlInsertMP3 = "insert into mp3 (author_id2, name) VALUES (:authorId, :mp3Name)";

        params = new MapSqlParameterSource();
        params.addValue("mp3Name", mp3.getName());
        params.addValue("authorId", author_id);

        return jdbcTemplate.update(sqlInsertMP3, params);
    }

    @Override
    public int insertList(List<MP3> listMP3) {
        String sql = "insert into mp3 (name, author) VALUES (:author, :name)";

        SqlParameterSource[] params = new SqlParameterSource[listMP3.size()];
        int i=0;

        for (MP3 mp3: listMP3) {
            MapSqlParameterSource p = new MapSqlParameterSource();
            p.addValue("name", mp3.getName());
            p.addValue("author", mp3.getAuthor());
            params[i] = p;
            i++;
        }
        int[] updateCount = jdbcTemplate.batchUpdate(sql, params);
        return updateCount.length;

    }

    @Override
    public Map<String, Integer> getStat() {
        String sql = "select author_name, count(*) as count from " + mp3View + " group by author_name";
        return jdbcTemplate.query(sql, new ResultSetExtractor<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<String, Integer> map = new TreeMap<>();
                while (rs.next()) {
                    String author = rs.getString("author_name");
                    int count = rs.getInt("count");
                    map.put(author, count);
                }

                return map;
            }
        });
    }

    @Override
    public void delete(int id) {
        String sql = "delete from mp3 where id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        jdbcTemplate.update(sql, params);
    }

    @Override
    public List<MP3> getMP3ListByName(String mp3Name) {
        String sql = "select * from " + mp3View + " where upper(mp3_name) like :mp3_name";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("mp3_name", "%" + mp3Name.toUpperCase() +"%");
        return jdbcTemplate.query(sql,params, new MP3RowMapper());
    }

    @Override
    public List<MP3> getMP3ListByAuthor(String author) {
        String sql = "select * from " + mp3View + " where upper(author_name) like :author_name";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("author_name", "%" + author.toUpperCase() +"%");
        return jdbcTemplate.query(sql,params, new MP3RowMapper());
    }

    @Override
    public int getMP3Count() {
        String sql = "select count(*) from " + mp3Table;

        return jdbcTemplate.getJdbcOperations().queryForObject(sql, Integer.class);
    }
}
