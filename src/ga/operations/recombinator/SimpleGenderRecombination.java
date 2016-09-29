package ga.operations.recombinator;

import com.sun.istack.internal.NotNull;
import ga.components.chromosome.SimpleGenderDiploid;
import ga.components.genes.Gene;
import ga.components.hotspots.Hotspot;
import ga.components.materials.SimpleDNA;
import ga.operations.dominanceMappings.DominanceMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by david on 28/09/16.
 */
public class SimpleGenderRecombination implements Recombinator<SimpleGenderDiploid> {

    private int numOfChildren;

    public SimpleGenderRecombination(final int numOfChildren) {
        setNumOfChildren(numOfChildren);
    }

    private void filter(final int numOfChildren) {
        if (numOfChildren < 1) throw new IllegalArgumentException("Number of children must be at least 1");
    }

    @Override
    public List<SimpleGenderDiploid> recombine(@NotNull final List<SimpleGenderDiploid> mates) {
        List<SimpleGenderDiploid> children = new ArrayList<>(numOfChildren);
        for (int i = 0; i < numOfChildren; i++) children.add(reproduce(mates));
        return children;
    }

    private SimpleGenderDiploid reproduce(@NotNull final List<SimpleGenderDiploid> mates) {
        SimpleGenderDiploid father = mates.get(0);
        SimpleGenderDiploid mother = mates.get(1);
        List<SimpleDNA> maleGametes = crossover(father);
        List<SimpleDNA> femaleGametes = crossover(mother);
        final int maleMatch = ThreadLocalRandom.current().nextInt(maleGametes.size());
        final int femaleMatch = ThreadLocalRandom.current().nextInt(femaleGametes.size());
        final boolean masculine = ThreadLocalRandom.current().nextBoolean();
        final Hotspot hotspot = (Hotspot) ((masculine) ? father.getHotspot().copy() : mother.getHotspot().copy());
        final DominanceMapping mapping = (DominanceMapping) ((masculine) ? father.getMapping().copy() : mother.getMapping().copy());
        return new SimpleGenderDiploid(maleGametes.get(maleMatch),
                                       femaleGametes.get(femaleMatch),
                                       mapping, hotspot, masculine);
    }

    private List<SimpleDNA> crossover(@NotNull final SimpleGenderDiploid parent) {
        List<SimpleDNA> gametes = new ArrayList<>(2);
        List<SimpleDNA> materialView = parent.getMaterialsView();
        SimpleDNA dna1 = materialView.get(0).copy();
        SimpleDNA dna2 = materialView.get(1).copy();
        List<Double> recombinationRate = parent.getHotspot().getRecombinationRate();
        for (int i = 0; i < recombinationRate.size(); i++) {
            if (Math.random() < recombinationRate.get(i)) {
                Gene gene1 = dna1.getGene(i);
                Gene gene2 = dna2.getGene(i);
                Object value = gene1.getValue();
                gene1.setValue(gene2.getValue());
                gene2.setValue(value);
            }
        }
        gametes.add(dna1);
        gametes.add(dna2);
        return gametes;
    }

    public int getNumOfChildren() {
        return numOfChildren;
    }

    public void setNumOfChildren(final int numOfChildren) {
        filter(numOfChildren);
        this.numOfChildren = numOfChildren;
    }
}