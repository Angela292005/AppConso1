package com.example.rojasquispeangeladeniss

/**
 * Criterio 1: Clase Colaborador
 * Representa la información base de un trabajador o colaborador
 * de la empresa de transportes RUTAS S.A.C.
 *
 * @property nombre Nombre completo del colaborador
 * @property area Área de trabajo (ej. Transporte, Encomiendas, Operaciones)
 * @property añosExperiencia Años de antigüedad o experiencia en la empresa
 */
data class Colaborador(
    val nombre: String,
    val area: String,
    val añosExperiencia: Int
)
