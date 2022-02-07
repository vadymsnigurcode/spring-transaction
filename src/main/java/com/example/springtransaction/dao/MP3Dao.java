package com.example.springtransaction.dao;

import java.util.List;
import java.util.Map;

public interface MP3Dao {
    int insert(MP3 mp3);
    int insertList(List<MP3> listMP3);
    List<MP3> getMP3ListByName(String mp3Name);
    List<MP3> getMP3ListByAuthor(String author);
    int getMP3Count();
    Map<String, Integer> getStat();
    void delete(int id);
    MP3 getMP3ById(int id);

}
