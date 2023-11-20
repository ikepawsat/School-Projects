/*
 * Author: Ike Pawsat - pawsat@bc.edu
 * Program allows the user to visualize the Central Limit Theorem
 */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define SAMPLES 10000
#define RUNS 50000
#define BINS 64
#define HISTOGRAM_SPAN 0.05
#define SCALE 32

double get_mean_of_uniform_random_samples() {
    double sum = 0.0;
    for (int i = 0; i < SAMPLES; i++) {
        double random_num = (((double)rand() / RAND_MAX) * 2.0) - 1.0;
        sum += random_num;
    }
    return sum / SAMPLES;
}

double populate_values_and_get_mean(double *values) {
    double sum = 0.0;
    for (int i = 0; i < RUNS; i++) {
        values[i] = get_mean_of_uniform_random_samples();
        sum += values[i];
    }
    return sum / RUNS;
}

double get_mean_squared_error(double *values, double mean) {
    double errors_squared = 0.0;
    for (int i = 0; i < RUNS; i++) {
        double error = values[i] - mean;
        errors_squared += error * error;
    }
    return errors_squared / RUNS;
}

void create_histogram(double *values, int *counts) {
    double bin_size = HISTOGRAM_SPAN / BINS;
    for (int i = 0; i < RUNS; i++) {
        int j;
        double bin_start;
        for (j = 0, bin_start = -(HISTOGRAM_SPAN / 2.0); j < BINS; ++j, bin_start += bin_size) {
            double bin_end = bin_start + bin_size;
            if (values[i] >= bin_start && values[i] < bin_end) {
                counts[j]++;
                break;
            }
        }
    }
}

void print_histogram(int *counts) {
    double bin_size = HISTOGRAM_SPAN / BINS;
    for (int i = 0; i < BINS; i++) {
        printf("%.4f  ", -(HISTOGRAM_SPAN / 2.0) + i * bin_size + bin_size / 2.0);
        int scaled_count = counts[i] / SCALE;
        for (int j = 0; j < scaled_count; j++) {
            printf("X");
        }
        printf("\n");
    }
}

int main() {
    srand(time(NULL)); // Allows for multiple random/new generations

    double *values = (double *)malloc(RUNS * sizeof(double));
    double averages = populate_values_and_get_mean(values);
    double error = get_mean_squared_error(values, averages);
    int counts[BINS] = {0};

    create_histogram(values, counts);

    print_histogram(counts);
    printf("Sample mean: %f    Sample variance: %f\n", averages, error);

    free(values); // since assigning want to prevent stack overflow
    return EXIT_SUCCESS;
}

