package pl.edu.pg.eti.data.repository

import pl.edu.pg.eti.domain.repository.LoginRepository

class LoginRepositoryImpl: LoginRepository {

    override fun login(): Boolean {
        //no i tutaj sobie mozemy zawołać service, który zrobi nam zapytanie do api
        // lub cokolwiek innego z logiki biznesowej
        TODO()
    }

}