package com.example.springtransaction.dao;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MP3RowMapper implements RowMapper<MP3> {

    @Override
    public MP3 mapRow(ResultSet rs, int rowNum) throws SQLException {
        Author author = new Author();
        author.setId(rs.getInt("author_id"));
        author.setName("author_name");
        MP3 mp3 = new MP3();
        mp3.setId(rs.getInt("mp3_id"));
        mp3.setName(rs.getString("mp3_name"));
        mp3.setAuthor(author);

        return mp3;
    }
}
