/*
 * Author: Ike Pawsat - pawsat@bc.edu
 * Comparison of two methods of Matrix Multiplication.
 */

#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>

#define FALSE 0
#define MAX_DIM_POWER 10
#define MAX_VALUE 20
#define MIN_DIM_POWER 3
#define TRUE 1
// weird order ^ but alphabetized

void init(const int dim, int * const m) {
    for (int i = 0; i < dim * dim; i++) {
        m[i] = rand() % MAX_VALUE;
    }
}

void multiply(const int dim, const int * const a, int * const b, int * const c) {
    for (int i = 0; i < dim; i++) {
        for (int j = 0; j < dim; j++) {
            for (int k = 0; k < dim; k++) {
                c[i * dim + j] += a[i * dim + k] * b[k * dim + j];
            }
        }
    }
}

void transpose(const int dim, int * const m) {
    for (int i = 0; i < dim; i++) {
        for (int j = 0; j < i; j++) {
            int temp = m[i * dim + j];
            m[i * dim + j] = m[j * dim + i];
            m[j * dim + i] = temp;
        }
    }
}

void multiply_transpose(const int dim, const int * const a, const int * const b_t, int * const c) {
    for (int i = 0; i < dim; i++) {
        for (int j = 0; j < dim; j++) {
            for (int k = 0; k < dim; k++) {
                c[i * dim + j] += a[i * dim + k] * b_t[j * dim + k];
            }
        }
    }
}

void transpose_and_multiply(const int dim, const int * const a, int * const b, int * const c) {
    transpose(dim, b);
    multiply_transpose(dim, a, b, c);
}

struct timeval run_and_time(
        void (* mult_func)(const int, const int * const, int * const, int * const),
        const int dim,
        const int * const a,
        int * const b,
        int * const c
) {
    struct timeval start, end;
    gettimeofday(&start, NULL);
    mult_func(dim, a, b, c);
    gettimeofday(&end, NULL);

    if (end.tv_usec < start.tv_usec) {
        end.tv_sec--;
        end.tv_usec += 1000000;
    }
    end.tv_sec -= start.tv_sec;
    end.tv_usec -= start.tv_usec;

    return end;
}

int verify(const int dim, const int * const c1, const int * const c2) {
    for (int i = 0; i < dim * dim; i++) {
        if (c1[i] != c2[i]) {
            return FALSE;
        }
    }
    return TRUE;
}

void run_test(const int dim) {
    int *a = (int *)calloc(dim * dim, sizeof(int));
    int *b = (int *)calloc(dim * dim, sizeof(int));
    int *c = (int *)calloc(dim * dim, sizeof(int));
    int *c2 = (int *)calloc(dim * dim, sizeof(int));

    init(dim, a);
    init(dim, b);

    struct timeval standard_time = run_and_time(&multiply, dim, a, b, c);
    struct timeval transposed_time = run_and_time(&transpose_and_multiply, dim, a, b, c2);

    printf("Results %s\n", verify(dim, c, c2) == TRUE ? "agree." : "disagree.");
    printf("Standard multiplication: %ld seconds and %d microseconds\n", standard_time.tv_sec, standard_time.tv_usec);
    printf("Multiplication with transpose: %ld seconds and %d microseconds\n", transposed_time.tv_sec, transposed_time.tv_usec);

    free(a);
    free(b);
    free(c);
    free(c2);
}

void print(const int dim, const int * const m) {
    for (int i = 0; i < dim; i++) {
        for (int j = 0; j < dim; j++) {
            printf("%d\t", m[i * dim + j]);
        }
        printf("\n");
    }
}

int main() {
    for (int power = MIN_DIM_POWER; power <= MAX_DIM_POWER; power++) {
        run_test(1 << power);
    }
    return EXIT_SUCCESS;
}

