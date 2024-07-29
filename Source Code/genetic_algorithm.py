#Genetic Algorithm Implementation

#Import Necessary libraries
from random import randint

#Population size
pop_size = 100
#Parent count
parent_count = 10
#The target string to be evolved
target_string = "Welcome to CS547!"
#Possible characters in the string
population = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 !"

#Mutation Rate
mut_rate = 5


# Calculate fitness
def evaluate(indv):
    fitness = 0
    for i in range(len(indv)):
        if indv[i] == target_string[i]:
            fitness += 1
    return fitness


# Genetic algorithm
def genetic_algorithm(pop, goal):
    iterations = 0

    while True:
        # calculate fitness
        for current_indv in pop.keys():
            pop[current_indv] = evaluate(current_indv)

        #Find matches in population
        for current_indv, fitness in pop.items():
            if fitness == len(goal):
                return iterations

        # Parent selection
        ancestors = sorted(pop.items(), key=lambda x: x[1], reverse=True)[:parent_count]
        print(ancestors[0])
        ancestors = dict(ancestors)

        # Mutate
        delete = []
        add = []
        for current_indv, fitness in ancestors.items():
            mutate = randint(1, 100)
            if mutate <= mut_rate:
                rand_char = population[randint(0, len(population) - 1)]
                rand_index = randint(0, len(current_indv) - 1)
                replace_string = list(current_indv)
                replace_string[rand_index] = rand_char
                delete.append(current_indv)
                add.append("".join(replace_string))

        for indv in delete:
            ancestors.pop(indv)

        for indv in add:
            ancestors[indv] = -1

        # Crossover
        pop = {}
        for current_indv in ancestors.keys():
            for crossover in ancestors.keys():
                if not current_indv == crossover:
                    offspring = current_indv[:6] + crossover[6:]
                    pop[offspring] = -1

        iterations += 1


def main():

    # Initialise population 
    initial_population = {}

    # Generate string from population
    for i in range(pop_size):
        gen_string = ""

        for j in range(len(target_string)):
            rand = randint(0, len(population) - 1)
            gen_string += population[rand]

        initial_population[gen_string] = -1

    #Evolve string
    evolve = genetic_algorithm(initial_population, target_string)
    print(evolve)

if __name__ == "__main__":
    main()
