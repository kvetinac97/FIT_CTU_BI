#include<stdio.h>
#include<math.h>
#include<float.h>

double cosDeg(double angle){
    return cos((angle) * M_PI / 180.0);
}

double sinDeg(double angle){
    return sin((angle) * M_PI / 180.0);
}

int main(void){
    printf("Trojuhelnik #1:\n");
    double a1 = -1, b1 = -1, c1 = -1, alpha1 = -1, beta1 = -1, gamma1 = -1;
    char pism1, pism2, pism3;
    double cislo1, cislo2, cislo3;
    
    if (scanf(" %c%c%c %lf %lf %lf", &pism1, &pism2, &pism3, &cislo1, &cislo2, &cislo3) != 6){
        printf("Nespravny vstup.\n");
        return 1;
    }
    
    /* zkusíme načíst SSS */
    if (pism1 == 'S' && pism2 == 'S' && pism3 == 'S'){
        a1 = cislo1;
        b1 = cislo2;
        c1 = cislo3;
        
        if (a1 <= 0 || b1 <= 0 || c1 <= 0){
            printf("Nespravny vstup.\n");
            return 1;
        }
        
        double chybaScitani1 = 1000 * DBL_EPSILON * a1 * b1 * c1;
        
        if ((a1 + b1 - c1) <= chybaScitani1 || (b1 + c1 - a1) <= chybaScitani1 || (a1 + c1 - b1) <= chybaScitani1) {
            printf("Vstup netvori trojuhelnik.\n");
            return 1;
        }
    }
    else {
        a1 = b1 = c1 = -1;
        
        /* zkusíme načíst SUS */
        if (pism1 == 'S' && pism2 == 'U' && pism3 == 'S'){
            a1 = cislo1;
            gamma1 = cislo2;
            b1 = cislo3;
            
            if (a1 <= 0 || b1 <= 0){
                printf("Nespravny vstup.\n");
                return 1;
            }

            if (gamma1 <= 0 || gamma1 >= 180){
                printf("Nespravny vstup.\n");
                return 1;
            }
        }
        else {
            a1 = b1 = gamma1 = -1;
            
            /* zkusíme načíst USU */
            if (pism1 == 'U' && pism2 == 'S' && pism3 == 'U'){
                beta1 = cislo1;
                a1 = cislo2;
                gamma1 = cislo3;
                
                if (beta1 <= 0 || beta1 >= 180){
                    printf("Nespravny vstup.\n");
                    return 1;
                }
                
                if (gamma1 <= 0 || gamma1 >= 180){
                    printf("Nespravny vstup.\n");
                    return 1;
                }
                
                if (a1 <= 0){
                    printf("Nespravny vstup.\n");
                    return 1;
                }
                
                double chybaScitani2 = 1000 * DBL_EPSILON * beta1 * gamma1;

                if (beta1 + gamma1 <= chybaScitani2 || !((beta1 + gamma1 - 180) < chybaScitani2) || (beta1 + gamma1) == 180){
                    printf("Vstup netvori trojuhelnik.\n");
                    return 1;
                }
            }
            else {
                printf("Nespravny vstup.\n");
                return 1;
            }
        }
    }
    
    double swap;
    if (a1 > b1){
        swap = a1;
        a1 = b1;
        b1 = swap;
    }
    if (a1 > c1){
        swap = a1;
        a1 = c1;
        c1 = swap;
    }
    if (b1 > c1){
        swap = b1;
        b1 = c1;
        c1 = swap;
    }
    
    if (alpha1 > beta1){
        swap = alpha1;
        alpha1 = beta1;
        beta1 = swap;
    }
    if (alpha1 > gamma1){
        swap = alpha1;
        alpha1 = gamma1;
        gamma1 = swap;
    }
    if (beta1 > gamma1){
        swap = beta1;
        beta1 = gamma1;
        gamma1 = swap;
    }
            
    printf("Trojuhelnik #2:\n");
    double a2 = -1, b2 = -1, c2 = -1, alpha2 = -1, beta2 = -1, gamma2 = -1;
    char pis1, pis2, pis3;
    double cis1, cis2, cis3;
    
    if (scanf(" %c%c%c %lf %lf %lf", &pis1, &pis2, &pis3, &cis1, &cis2, &cis3) != 6){
        printf("Nespravny vstup.\n");
        return 1;
    }
        
    /* zkusíme načíst SSS */
    if (pis1 == 'S' && pis2 == 'S' && pis3 == 'S'){
        a2 = cis1;
        b2 = cis2;
        c2 = cis3;
        
        if (a2 <= 0 || b2 <= 0 || c2 <= 0){
            printf("Nespravny vstup.\n");
            return 1;
        }
        
        double chybascitani3 = 1000 * DBL_EPSILON * a2 * b2 * c2;
        
        if ((a2 + b2 - c2) <= chybascitani3 || (b2 + c2 - a2) <= chybascitani3 || (a2 + c2 - b2) <= chybascitani3) {
            printf("Vstup netvori trojuhelnik.\n");
            return 1;
        }
    }
    else {
        a2 = b2 = c2 = -1;
        
        /* zkusíme načíst SUS */
        if (pis1 == 'S' && pis2 == 'U' && pis3 == 'S'){
            a2 = cis1;
            gamma2 = cis2;
            b2 = cis3;
            
            if (a2 <= 0 || b2 <= 0){
                printf("Nespravny vstup.\n");
                return 1;
            }
            
            if (gamma2 <= 0 || gamma2 >= 180){
                printf("Nespravny vstup.\n");
                return 1;
            }
        }
        else {
            a2 = b2 = gamma2 = -1;
            
            /* zkusíme načíst USU */
            if (pis1 == 'U' && pis2 == 'S' && pis3 == 'U'){
                beta2 = cis1;
                a2 = cis2;
                gamma2 = cis3;
                
                if (a2 <= 0){
                    printf("Nespravny vstup.\n");
                    return 1;
                }
                
                if (beta2 <= 0 || beta2 >= 180){
                    printf("Nespravny vstup.\n");
                    return 1;
                }
                
                if (gamma2 <= 0 || gamma2 >= 180){
                    printf("Nespravny vstup.\n");
                    return 1;
                }
                
                double chybascitani4 = 1000 * DBL_EPSILON * beta2 * gamma2;

                if (beta2 + gamma2 <= chybascitani4 || !((beta2 + gamma2 - 180) < chybascitani4) || (beta2 + gamma2) == 180){
                    printf("Vstup netvori trojuhelnik.\n");
                    return 1;
                }
            }
            else {
                printf("Nespravny vstup.\n");
                return 1;
            }
        }
    }
    
    if (a2 > b2){
        swap = a2;
        a2 = b2;
        b2 = swap;
    }
    if (a2 > c2){
        swap = a2;
        a2 = c2;
        c2 = swap;
    }
    if (b2 > c2){
        swap = b2;
        b2 = c2;
        c2 = swap;
    }
    
    if (alpha2 > beta2){
        swap = alpha2;
        alpha2 = beta2;
        beta2 = swap;
    }
    if (alpha2 > gamma2){
        swap = alpha2;
        alpha2 = gamma2;
        gamma2 = swap;
    }
    if (beta2 > gamma2){
        swap = beta2;
        beta2 = gamma2;
        gamma2 = swap;
    }
    
    // Konverze na a, b, c :/
        
    // SUS -> SSS
    if ( b1 != -1 && c1 != -1 && gamma1 != -1 ) {
        a1 = sqrt(fabs(b1*b1 + c1*c1 - 2*b1*c1*cosDeg(gamma1)));
    }
    
    if ( b2 != -1 && c2 != -1 && gamma2 != -1 ) {
        a2 = sqrt(fabs(b2*b2 + c2*c2 - 2*b2*c2*cosDeg(gamma2)));
    }
    
    // USU -> SSS
    if ( c1 != -1 && beta1 != -1 && gamma1 != -1 ) {
        a1 = (sinDeg(beta1) * c1) / sinDeg(180 - (beta1 + gamma1));
        b1 = (sinDeg(gamma1) * c1) / sinDeg(180 - (beta1 + gamma1));
    }
    
    if ( c2 != -1 && beta2 != -1 && gamma2 != -1 ) {
        a2 = (sinDeg(beta2) * c2) / sinDeg(180 - (beta2 + gamma2));
        b2 = (sinDeg(gamma2) * c2) / sinDeg(180 - (beta2 + gamma2));
    }
    
    //Převod zpět
    
    if (a1 > b1){
        swap = a1;
        a1 = b1;
        b1 = swap;
    }
    if (a1 > c1){
        swap = a1;
        a1 = c1;
        c1 = swap;
    }
    if (b1 > c1){
        swap = b1;
        b1 = c1;
        c1 = swap;
    }
    
    if (a2 > b2){
        swap = a2;
        a2 = b2;
        b2 = swap;
    }
    if (a2 > c2){
        swap = a2;
        a2 = c2;
        c2 = swap;
    }
    if (b2 > c2){
        swap = b2;
        b2 = c2;
        c2 = swap;
    }
    
    //Kontrola po převedení
    double chybaNasobeni3 = 100 * DBL_EPSILON * b2 * a1 * c1;

    if (fabs(a1 - a2) <= chybaNasobeni3 && fabs(b1 - b2) <= chybaNasobeni3 && fabs(c1 - c2) <= chybaNasobeni3){
        printf("Trojuhelniky jsou shodne.\n");
        return 0;
    }
    
    if ( fabs((a1*b2)-(b1*a2)) <= chybaNasobeni3 && fabs((b1*c2)-(c1*b2)) <= chybaNasobeni3  ) {
        printf("Trojuhelniky nejsou shodne, ale jsou podobne.\n");
        return 0;
    }
    
    printf("Trojuhelniky nejsou shodne ani podobne.\n");
    return 0;
}
