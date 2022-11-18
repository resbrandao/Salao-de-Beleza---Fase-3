package br.pro.israelsousa.gestaosalaobeleza.home

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import br.pro.israelsousa.gestaosalaobeleza.databinding.ActivityAgendamentoBinding
import br.pro.israelsousa.gestaosalaobeleza.R
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class AgendamentoTest{

@Test
    fun testActivityVisible(){
        val activityCenario = ActivityScenario.launch(Agendamento::class.java)
        onView(withId(R.id.agendamento)).check(matches(isDisplayed()))

    }

}