package com.chin.gameserver.application.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/19 20:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    private Integer id;

    // -1为人工
    private Integer botId;
    private String botCode;

    private Integer sx;
    private Integer sy;

    // 存路径
    private List<Integer> steps;

    public List<Cell> getCell() {
        List<Cell> res = new ArrayList<>();
        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        res.add(new Cell(x, y));
        int step = 0;
        for (int d : steps) {
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));
            if (!checkTailIncreasing(++ step)) {
                res.remove(0);
            }
        }
        return res;
    }

    public boolean checkTailIncreasing(int step) {
        if (step <= 10) {
            return true;
        }
        return step % 3 == 1;
    }

    public String getStepsString() {
        StringBuilder res = new StringBuilder();
        for (int d: steps) {
            res.append(d);
        }
        return res.toString();
    }

}
