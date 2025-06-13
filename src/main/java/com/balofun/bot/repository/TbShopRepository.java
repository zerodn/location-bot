package com.balofun.bot.repository;

import com.balofun.bot.dto.TbShop;
import com.resdii.noodev.maria.repository.MariaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface TbShopRepository extends MariaRepository<TbShop, BigInteger> {
    Optional<TbShop> findFirstByHash(long hash);
}