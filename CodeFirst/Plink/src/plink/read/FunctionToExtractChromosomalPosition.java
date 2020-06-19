/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.read;

/**
 *
 * @author Oscar Lao
 */
public abstract class FunctionToExtractChromosomalPosition {
    
    public abstract byte retrieve_chromosome(String f) throws NumberFormatException;
    
}
