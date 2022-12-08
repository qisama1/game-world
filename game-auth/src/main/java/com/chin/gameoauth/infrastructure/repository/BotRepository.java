package com.chin.gameoauth.infrastructure.repository;

import com.chin.gameoauth.domain.domain.model.VO.BotVO;
import com.chin.gameoauth.domain.domain.repository.IBotRepository;
import com.chin.gameoauth.domain.security.model.UserDetailsImpl;
import com.chin.gameoauth.domain.security.model.VO.UserDetail;
import com.chin.gameoauth.infrastructure.dao.IBotDao;
import com.chin.gameoauth.infrastructure.pojo.Bot;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qi
 */
@Component
public class BotRepository implements IBotRepository {

    @Resource
    private IBotDao botDao;

    @Override
    public List<BotVO> queryBotList() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        UserDetail userDetail = user.getUserDetail();
        List<Bot> bots = botDao.queryBotByUserId(userDetail.getId());
        List<BotVO> botVOS = new ArrayList<>();
        for (Bot bot : bots) {
            BotVO botVO = new BotVO();
            botVO.setTitle(bot.getTitle());
            botVO.setContent(bot.getContent());
            botVO.setDescription(bot.getDescription());
            botVO.setId(bot.getId());
            botVO.setUserId(bot.getUserId());
            botVOS.add(botVO);
        }
        return botVOS;
    }

    @Override
    public BotVO queryBotById(Integer id) {
        Bot bot = botDao.queryBotById(id);
        BotVO botVo = new BotVO();
        botVo.setUserId(bot.getUserId());
        botVo.setDescription(bot.getDescription());
        botVo.setContent(bot.getContent());
        botVo.setTitle(bot.getTitle());
        botVo.setId(bot.getId());
        return botVo;
    }

    @Override
    public void insertBot(String content, String title, String description) {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        UserDetail userDetail = user.getUserDetail();
        Bot bot = new Bot();
        bot.setUserId(userDetail.getId());
        bot.setContent(content);
        bot.setTitle(title);
        bot.setDescription(description);
        botDao.insertBot(bot);
    }

    @Override
    public boolean updateBot(Integer id, String content, String title, String description) {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        UserDetail userDetail = user.getUserDetail();
        Bot oldBot = botDao.queryBotById(id);
        if (!oldBot.getUserId().equals(userDetail.getId())) {
            return false;
        }
        Bot bot = new Bot();
        bot.setId(id);
        bot.setUserId(oldBot.getUserId());
        bot.setContent(content);
        bot.setTitle(title);
        bot.setDescription(description);
        return botDao.updateBot(bot);
    }

    @Override
    public boolean deleteBot(Integer id, Integer userId) {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        UserDetail userDetail = user.getUserDetail();
        if (!userId.equals(userDetail.getId())) {
            return false;
        }
        Bot bot = new Bot();
        bot.setId(id);
        return botDao.deleteBot(bot);
    }
}
