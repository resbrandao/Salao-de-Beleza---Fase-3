package br.pro.israelsousa.gestaosalaobeleza.login

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import br.pro.israelsousa.gestaosalaobeleza.R
import br.pro.israelsousa.gestaosalaobeleza.home.Agendamento
import org.junit.Assert.*
import org.junit.Test

class RecuperaContaTest{

    @Test
    fun testActivityVisible(){
        val activityCenario = ActivityScenario.launch(Agendamento::class.java)
        Espresso.onView(ViewMatchers.withId(R.id.recupera_conta))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

}