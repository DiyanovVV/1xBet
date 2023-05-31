package com.fonsport.bookmakerapp.model.repository

import com.bookmaker.onexbetapp.model.repository.PlaymakerRepository


class Repository {
    companion object{
        var playmaker = PlaymakerRepository()
    }
}