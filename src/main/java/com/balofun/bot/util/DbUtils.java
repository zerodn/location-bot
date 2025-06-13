package com.balofun.bot.util;

import com.balofun.bot.dto.TbShop;
import com.balofun.bot.repository.TbShopRepository;

public class DbUtils {

    public static boolean saveLocation(TbShopRepository tbShopRepository, String url, String textSearch) {

        long hash = StringUtils.hashUrlToLong(url);
        if (!tbShopRepository.findFirstByHash(hash).isPresent()) {
            TbShop tbShop = new TbShop();
            tbShop.setUrl(url);
            tbShop.setText_search(textSearch);
            tbShop.setHash(hash);
            tbShopRepository.saveAndFlush(tbShop);
            System.out.println("saveLocation: " + url);
        }

        return true;
    }
}
