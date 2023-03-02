package com.yyhdbl.dto;

import com.yyhdbl.entity.Setmeal;
import com.yyhdbl.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
