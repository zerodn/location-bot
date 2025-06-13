package com.balofun.bot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "shop")
@Data
public class TbShop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public BigInteger id;
    public String url;
    public long hash;
    public String text_search;
}