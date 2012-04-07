package de.mh4j.solver.simulatedAnnealing;

import de.mh4j.solver.AbstractSolver;
import de.mh4j.solver.Solution;

/**
 * Local search algorithms move from solution to solution in the search space by
 * applying local changes, until the termination condition is reached. <br>
 * <br>
 * <b>Description:</b><br>
 * Every local search algorithm starts from an initial candidate solution and
 * then iteratively moves to a neighbor of that solution.<br>
 * If the neighbor is better than the current solution it becomes the new
 * candidate solution. If the neighbor was worse the current solution stays as
 * candidate. These steps are repeated until the termination condition has been
 * reached.<br>
 * <br>
 * Possible termination conditions may be a fixed time interval or the number of
 * steps that the algorithm could not produce a better solution candidate.<br>
 * <br>
 * The problem with pure local search algorithm is that they can easily run into
 * locally optimal solutions and may never return from there. This local-optima
 * problem may be cured by using restarts (repeated local search with different
 * initial conditions), or more advanced local search algorithms like
 * {@link SimulatedAnnealingSolver Simulated Annealing}.
 * 
 * @author Friedrich Große
 * 
 * @param <GenericSolutionType>
 *            The Type of the class that describes a candidate solution for the
 *            given optimization problem.
 * 
 * @see SimulatedAnnealingSolver
 */
public abstract class AbstractLocalSearchSolver<GenericSolutionType extends Solution> extends
        AbstractSolver<GenericSolutionType> {

    protected int situationHasNotImproved = 0;
    protected GenericSolutionType currentSolution;

    /**
     * Creates a new local search solver with the current system time as seed
     * for the randomizer.
     */
    public AbstractLocalSearchSolver() {
        this(System.currentTimeMillis());
    }

    /**
     * Creates a new local search solver with the given seed for its randomizer.
     * This is useful if you want to recreate results that have been produced
     * earlier with a specific seed.
     */
    public AbstractLocalSearchSolver(long seed) {
        super(seed);
    }

    @Override
    public GenericSolutionType getCurrentSolution() {
        return currentSolution;
    }

    @Override
    protected void doInitialize() {
        situationHasNotImproved = 0;
        currentSolution = createInitialSolution();
        log.debug("Initial solution with costs {} created: {}", currentSolution.getCosts(), currentSolution);
    }

    @Override
    protected void doStep() {
        GenericSolutionType neighbor = createRandomNeighbor();

        if (neighbor.isBetterThan(currentSolution)) {
            currentSolution = neighbor;
            situationHasNotImproved = 0;
            log.debug("Found a better neighbor. New costs are {}", neighbor.getCosts());
        }
        else {
            situationHasNotImproved++;
            log.debug("Neighbor is worse than the current configuration. Costs stay at {}", currentSolution.getCosts());
        }
    }

    /**
     * @return The initial tour from where the local search optimization
     *         algorithm starts.
     */
    protected abstract GenericSolutionType createInitialSolution();

    /**
     * @return A randomly created neighbor of the current solution.
     */
    protected abstract GenericSolutionType createRandomNeighbor();

}
