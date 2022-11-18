package br.pro.israelsousa.gestaosalaobeleza.home

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import br.pro.israelsousa.gestaosalaobeleza.R
import org.junit.Assert.*
import org.junit.Test

class RetornaAgendamentoTest{

    @Test
    fun testActivityVisible(){
        val activityCenario = ActivityScenario.launch(Agendamento::class.java)
        Espresso.onView(ViewMatchers.withId(R.id.retorna_agendamento))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

}