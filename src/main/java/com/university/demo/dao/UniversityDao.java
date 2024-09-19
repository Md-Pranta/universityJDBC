package com.university.demo.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.demo.entity.University;
import com.university.demo.entity.UniversityType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class UniversityDao {
    private static final Logger logger = LoggerFactory.getLogger(UniversityDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    private final RowMapper<University> universityRowMapper = (rs, rowNum) -> {
        University university = new University();
        university.setId(rs.getLong("id"));
        university.setName(rs.getString("name"));
        university.setAddress(rs.getString("address"));
        university.setUniversityType(UniversityType.valueOf(rs.getString("university_type")));
        university.setRating(rs.getDouble("rating"));
        university.setDescription(rs.getString("description"));
        university.setImage(rs.getString("image"));
        university.setStartingDate(rs.getObject("starting_date", LocalDate.class));
        university.setCasuallyOpensAt(rs.getObject("casually_opens_at", LocalDateTime.class));

        String jsonString = rs.getString("other_information");
        try {
            Map otherInfo = objectMapper.readValue(jsonString, Map.class);
            university.setOtherInformation(otherInfo);
        } catch (Exception e) {
            throw new RuntimeException("c");
        }

        return university;
    };

//to save the post or update the exiting post
    public University saveUniversity(University university) throws JsonProcessingException {

        if (university.getId()==null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            final String sql = "INSERT INTO university (name, address, university_type, rating, description, image, starting_date, casually_opens_at, other_information) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CAST(? AS jsonb))";

            logger.debug("Executing SQL: " + sql);
            logger.debug("Parameters: name={}, address={}, university_type={}, rating={}, description={}, image_path={}, starting_date={}, casually_opens_at={}, other_information={}",
                    university.getName(), university.getAddress(), university.getUniversityType(), university.getRating(),
                    university.getDescription(), university.getImage(), university.getStartingDate(),
                    university.getCasuallyOpensAt(), university.getOtherInformation());

            try {

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, university.getName());
                ps.setString(2, university.getAddress());
                ps.setString(3, university.getUniversityType().toString());
                ps.setDouble(4, university.getRating());
                ps.setString(5, university.getDescription());
                ps.setString(6, university.getImage());
                ps.setObject(7, university.getStartingDate());
                ps.setObject(8, university.getCasuallyOpensAt());
                try {
                    ps.setString(9, objectMapper.writeValueAsString(university.getOtherInformation()));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("others"+e);
                }
                return ps;
            }, keyHolder);
            }catch (Exception e){            // Log the exact SQL error details
                logger.error("SQL Error: " + e.getMessage() + " - " + e.getMessage(), e);
                throw new RuntimeException("Error during university insert operation", e);
            }

            Number generatedId = keyHolder.getKey();
            if (generatedId != null) {
                university.setId(generatedId.longValue());
            } else {
                throw new RuntimeException("Failed to retrieve generated ID after inserting university");
            }
        }else {
            jdbcTemplate.update(
                    "UPDATE University SET name = ?, address = ?, university_type = ?, rating = ?, " +
                            "description = ?, image = ?, starting_date = ?, casually_opens_at = ?, other_information = ?::jsonb " +
                            "WHERE id = ?",
                    university.getName(),
                    university.getAddress(),
                    university.getUniversityType().toString(),
                    university.getRating(),
                    university.getDescription(),
                    university.getImage(),
                    university.getStartingDate(),
                    university.getCasuallyOpensAt(),
                    objectMapper.writeValueAsString(university.getOtherInformation()),
                    university.getId()
            );
        }
        return university;
    }

    public Optional<University> getUniversityById(Long id) {
        String sql = "SELECT * FROM university WHERE id = ?";

        List<University> ans =  jdbcTemplate.query("SELECT * FROM University WHERE id = ?",
                new Object[]{id},
                universityRowMapper);
        return ans.isEmpty() ? Optional.empty():Optional.of(ans.get(0));
    }

    public List<University> getAllUniversities() {
        String sql = "SELECT * FROM university";

        return jdbcTemplate.query(sql, universityRowMapper);
    }


    public void deleteUniversity(Long id) {
        String sql = "DELETE FROM university WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }

    public List<University> searchUniversities(String name, String address, UniversityType universityType, Double ratingFrom, Double ratingTo,String description ,LocalDateTime casuallyOpensAtFrom, LocalDateTime casuallyOpensAtTo) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM university WHERE 1=1");

        if (name != null) {
            sql.append(" AND name LIKE ?");
            params.add("%" + name + "%");
        }
        if (address != null) {
            sql.append(" AND address LIKE ?");
            params.add("%" + address + "%");
        }
        if (universityType != null) {
            sql.append(" AND university_type = ?");
            params.add(universityType.name());
        }
        if (ratingFrom != null) {
            sql.append(" AND rating >= ?");
            params.add(ratingFrom);
        }
        if (ratingTo != null) {
            sql.append(" AND rating <= ?");
            params.add(ratingTo);
        }
        if (description != null){
            sql.append(" AND description LIKE ?");
            params.add("%"+description+"%");
        }
        if (casuallyOpensAtFrom != null) {
            sql.append(" AND casually_opens_at >= ?");
            params.add(casuallyOpensAtFrom);
        }
        if (casuallyOpensAtTo != null) {
            sql.append(" AND casually_opens_at <= ?");
            params.add(casuallyOpensAtTo);
        }

        return jdbcTemplate.query(sql.toString(), params.toArray(), universityRowMapper);
    }



}

