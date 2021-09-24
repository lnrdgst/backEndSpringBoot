package com.viasoft.servnf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viasoft.servnf.model.ServNf;

@Repository
public interface ServNfRepository extends JpaRepository<ServNf, Long>{

}
