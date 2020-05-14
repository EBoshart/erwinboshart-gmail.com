package com.gramby.pictionary.draw;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coordinate {
    @NotNull
    private  Long DrawId;
    @NotNull
    private  int x;
    @NotNull
    private  int y;
}
