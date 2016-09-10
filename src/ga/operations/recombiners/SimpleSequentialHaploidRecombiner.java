package ga.operations.recombiners;

import com.sun.istack.internal.NotNull;
import ga.components.chromosome.SequentialHaploid;
import ga.components.genes.Gene;
import ga.components.materials.GeneticMaterial;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 8/09/16.
 */
public class SimpleSequentialHaploidRecombiner implements Recombiner<SequentialHaploid>{

    private double probability;

    public SimpleSequentialHaploidRecombiner(final double probability) {
        filter(probability);
        this.probability = probability;
    }

    private void filter(final double probability) {
        if (probability < 0 || probability > 1)
            throw new IllegalArgumentException("Invalid probability value.");
    }

    @Override
    public List<SequentialHaploid> recombine(@NotNull final List<SequentialHaploid> mates) {
        if (mates.size() != 2)
            throw new IllegalArgumentException("The input must consists of exactly 2 chromosomes.");
        SequentialHaploid hap1 = mates.get(0).copy();
        SequentialHaploid hap2 = mates.get(1).copy();
        GeneticMaterial material1 = hap1.getMaterialsView().get(0);
        GeneticMaterial material2 = hap2.getMaterialsView().get(1);
        for (int i = 0; i < hap1.getLength(); i++) {
            if (Math.random() < probability) {
                Gene gene1 = material1.getGene(i);
                Gene gene2 = material2.getGene(i);
                Object value1 = gene1.getValue();
                Object value2 = gene2.getValue();
                gene1.setValue(value2);
                gene2.setValue(value1);
            }
        }
        List<SequentialHaploid> children = new ArrayList<>(2);
        children.add(hap1);
        children.add(hap2);
        return children;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        filter(probability);
        this.probability = probability;
    }
}
