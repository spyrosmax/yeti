package tinyos.yeti.refactoring.entities.component.rename;

import java.util.Collection;

import tinyos.yeti.nesc12.parser.ast.nodes.general.Identifier;
import tinyos.yeti.refactoring.ast.AstAnalyzerFactory;
import tinyos.yeti.refactoring.rename.SelectionIdentifier;
import tinyos.yeti.refactoring.rename.alias.AliasSelectionIdentifier;

public class ComponentSelectionIdentifier extends SelectionIdentifier {

	public ComponentSelectionIdentifier(Identifier identifier) {
		super(identifier);
	}

	public ComponentSelectionIdentifier(Identifier identifier,AstAnalyzerFactory analyzerFactory) {
		super(identifier, analyzerFactory);
	}
	
	/**
	 * Checks if the given identifier is part of an AST node associated to an NesC component like a module or a configuration.
	 * @param identifier
	 * @return
	 */
	public boolean isComponent(Identifier identifier){
		return isComponentDefinition()
			||isComponentDeclaration()
			||isComponentWiringComponentPartNotAliased();
	}

	/**
	 * Checks if the given identifier is the identifier of a NesC Component Definition like a Module or a Configuration.
	 * @param identifier
	 * @return
	 */
	public boolean isComponentDefinition(){
		if(!factory4Selection.hasComponentAnalyzerCreated()){
			return false;
		}
		return componentAnalyzer.getEntityIdentifier()==identifier;
	}
	
	/**
	 * Checks if the given identifier is the component identifier of a NesC configuration' "components" statement.
	 * @param identifier
	 * @return
	 */
	public boolean isComponentDeclaration(){
		if(!factory4Selection.hasConfigurationAnalyzerCreated()){
			return false;
		}
		Collection<Identifier> identifiers=configurationAnalyzer.getIdentifiersOfReferencedComponents();
		return astUtil.containsIdentifierInstance(identifier,identifiers);
	}
	
	/**
	 * Checks if the given identifier is the component identifier of a NesC configuration wiring  statement, and if it is no alias.
	 * @param identifier
	 * @return
	 */
	public boolean isComponentWiringComponentPartNotAliased(){
		if(!factory4Selection.hasConfigurationAnalyzerCreated()){
			return false;
		}
		Collection<Identifier> identifiers=configurationAnalyzer.getWiringComponentPartIdentifiers();
		if(!astUtil.containsIdentifierInstance(identifier,identifiers)){
			return false;
		}
		AliasSelectionIdentifier selectionIdentifier=new AliasSelectionIdentifier(identifier,factory4Selection);
		return !selectionIdentifier.isComponentAlias();
	}
	
	

}