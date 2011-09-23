package gc.serverGC;

// Import declarations generated by the SALSA compiler, do not modify.
import java.io.IOException;
import java.util.Vector;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationTargetException;

import salsa.language.Actor;
import salsa.language.ActorReference;
import salsa.language.Message;
import salsa.language.RunTime;
import salsa.language.ServiceFactory;
import gc.WeakReference;
import salsa.language.Token;
import salsa.language.exceptions.*;
import salsa.language.exceptions.CurrentContinuationException;

import salsa.language.UniversalActor;

import salsa.naming.UAN;
import salsa.naming.UAL;
import salsa.naming.MalformedUALException;
import salsa.naming.MalformedUANException;

import salsa.resources.SystemService;

import salsa.resources.ActorService;

// End SALSA compiler generated import delcarations.

import gc.*;
import java.util.*;

public class CycleDetective extends UniversalActor  {
	public static void main(String args[]) {
		UAN uan = null;
		UAL ual = null;
		if (System.getProperty("uan") != null) {
			uan = new UAN( System.getProperty("uan") );
			ServiceFactory.getTheater();
			RunTime.receivedUniversalActor();
		}
		if (System.getProperty("ual") != null) {
			ual = new UAL( System.getProperty("ual") );

			if (uan == null) {
				System.err.println("Actor Creation Error:");
				System.err.println("	uan: " + uan);
				System.err.println("	ual: " + ual);
				System.err.println("	Identifier: " + System.getProperty("identifier"));
				System.err.println("	Cannot specify an actor to have a ual at runtime without a uan.");
				System.err.println("	To give an actor a specific ual at runtime, use the identifier system property.");
				System.exit(0);
			}
			RunTime.receivedUniversalActor();
		}
		if (System.getProperty("identifier") != null) {
			if (ual != null) {
				System.err.println("Actor Creation Error:");
				System.err.println("	uan: " + uan);
				System.err.println("	ual: " + ual);
				System.err.println("	Identifier: " + System.getProperty("identifier"));
				System.err.println("	Cannot specify an identifier and a ual with system properties when creating an actor.");
				System.exit(0);
			}
			ual = new UAL( ServiceFactory.getTheater().getLocation() + System.getProperty("identifier"));
		}
		RunTime.receivedMessage();
		CycleDetective instance = (CycleDetective)new CycleDetective(uan, ual,null).construct();
		gc.WeakReference instanceRef=new gc.WeakReference(uan,ual);
		{
			Object[] _arguments = { args };

			//preAct() for local actor creation
			//act() for remote actor creation
			if (ual != null && !ual.getLocation().equals(ServiceFactory.getTheater().getLocation())) {instance.send( new Message(instanceRef, instanceRef, "act", _arguments, false) );}
			else {instance.send( new Message(instanceRef, instanceRef, "preAct", _arguments, false) );}
		}
		RunTime.finishedProcessingMessage();
	}

	public static ActorReference getReferenceByName(UAN uan)	{ return new CycleDetective(false, uan); }
	public static ActorReference getReferenceByName(String uan)	{ return CycleDetective.getReferenceByName(new UAN(uan)); }
	public static ActorReference getReferenceByLocation(UAL ual)	{ return new CycleDetective(false, ual); }

	public static ActorReference getReferenceByLocation(String ual)	{ return CycleDetective.getReferenceByLocation(new UAL(ual)); }
	public CycleDetective(boolean o, UAN __uan)	{ super(false,__uan); }
	public CycleDetective(boolean o, UAL __ual)	{ super(false,__ual); }
	public CycleDetective(UAN __uan,UniversalActor.State sourceActor)	{ this(__uan, null, sourceActor); }
	public CycleDetective(UAL __ual,UniversalActor.State sourceActor)	{ this(null, __ual, sourceActor); }
	public CycleDetective(UniversalActor.State sourceActor)		{ this(null, null, sourceActor);  }
	public CycleDetective()		{  }
	public CycleDetective(UAN __uan, UAL __ual, Object obj) {
		//decide the type of sourceActor
		//if obj is null, the actor must be the startup actor.
		//if obj is an actorReference, this actor is created by a remote actor

		if (obj instanceof UniversalActor.State || obj==null) {
			  UniversalActor.State sourceActor;
			  if (obj!=null) { sourceActor=(UniversalActor.State) obj;}
			  else {sourceActor=null;}

			  //remote creation message sent to a remote system service.
			  if (__ual != null && !__ual.getLocation().equals(ServiceFactory.getTheater().getLocation())) {
			    WeakReference sourceRef;
			    if (sourceActor!=null && sourceActor.getUAL() != null) {sourceRef = new WeakReference(sourceActor.getUAN(),sourceActor.getUAL());}
			    else {sourceRef = null;}
			    if (sourceActor != null) {
			      if (__uan != null) {sourceActor.getActorMemory().getForwardList().putReference(__uan);}
			      else if (__ual!=null) {sourceActor.getActorMemory().getForwardList().putReference(__ual);}

			      //update the source of this actor reference
			      setSource(sourceActor.getUAN(), sourceActor.getUAL());
			      activateGC();
			    }
			    createRemotely(__uan, __ual, "gc.serverGC.CycleDetective", sourceRef);
			  }

			  // local creation
			  else {
			    State state = new State(__uan, __ual);

			    //assume the reference is weak
			    muteGC();

			    //the source actor is  the startup actor
			    if (sourceActor == null) {
			      state.getActorMemory().getInverseList().putInverseReference("rmsp://me");
			    }

			    //the souce actor is a normal actor
			    else if (sourceActor instanceof UniversalActor.State) {

			      // this reference is part of garbage collection
			      activateGC();

			      //update the source of this actor reference
			      setSource(sourceActor.getUAN(), sourceActor.getUAL());

			      /* Garbage collection registration:
			       * register 'this reference' in sourceActor's forward list @
			       * register 'this reference' in the forward acquaintance's inverse list
			       */
			      String inverseRefString=null;
			      if (sourceActor.getUAN()!=null) {inverseRefString=sourceActor.getUAN().toString();}
			      else if (sourceActor.getUAL()!=null) {inverseRefString=sourceActor.getUAL().toString();}
			      if (__uan != null) {sourceActor.getActorMemory().getForwardList().putReference(__uan);}
			      else if (__ual != null) {sourceActor.getActorMemory().getForwardList().putReference(__ual);}
			      else {sourceActor.getActorMemory().getForwardList().putReference(state.getUAL());}

			      //put the inverse reference information in the actormemory
			      if (inverseRefString!=null) state.getActorMemory().getInverseList().putInverseReference(inverseRefString);
			    }
			    state.updateSelf(this);
			    ServiceFactory.getNaming().setEntry(state.getUAN(), state.getUAL(), state);
			    if (getUAN() != null) ServiceFactory.getNaming().update(state.getUAN(), state.getUAL());
			  }
		}

		//creation invoked by a remote message
		else if (obj instanceof ActorReference) {
			  ActorReference sourceRef= (ActorReference) obj;
			  State state = new State(__uan, __ual);
			  muteGC();
			  state.getActorMemory().getInverseList().putInverseReference("rmsp://me");
			  if (sourceRef.getUAN() != null) {state.getActorMemory().getInverseList().putInverseReference(sourceRef.getUAN());}
			  else if (sourceRef.getUAL() != null) {state.getActorMemory().getInverseList().putInverseReference(sourceRef.getUAL());}
			  state.updateSelf(this);
			  ServiceFactory.getNaming().setEntry(state.getUAN(), state.getUAL(),state);
			  if (getUAN() != null) ServiceFactory.getNaming().update(state.getUAN(), state.getUAL());
		}
	}

	public UniversalActor construct () {
		Object[] __arguments = {  };
		this.send( new Message(this, this, "construct", __arguments, null, null) );
		return this;
	}

	public class State extends UniversalActor .State {
		public CycleDetective self;
		public void updateSelf(ActorReference actorReference) {
			((CycleDetective)actorReference).setUAL(getUAL());
			((CycleDetective)actorReference).setUAN(getUAN());
			self = new CycleDetective(false,getUAL());
			self.setUAN(getUAN());
			self.setUAL(getUAL());
			self.activateGC();
		}

		public void preAct(String[] arguments) {
			getActorMemory().getInverseList().removeInverseReference("rmsp://me",1);
			{
				Object[] __args={arguments};
				self.send( new Message(self,self, "act", __args, null,null,false) );
			}
		}

		public State() {
			this(null, null);
		}

		public State(UAN __uan, UAL __ual) {
			super(__uan, __ual);
			addClassName( "gc.serverGC.CycleDetective$State" );
			addMethodsForClasses();
		}

		public void process(Message message) {
			Method[] matches = getMatches(message.getMethodName());
			Object returnValue = null;
			Exception exception = null;

			if (matches != null) {
				if (!message.getMethodName().equals("die")) {message.activateArgsGC(this);}
				for (int i = 0; i < matches.length; i++) {
					try {
						if (matches[i].getParameterTypes().length != message.getArguments().length) continue;
						returnValue = matches[i].invoke(this, message.getArguments());
					} catch (Exception e) {
						if (e.getCause() instanceof CurrentContinuationException) {
							sendGeneratedMessages();
							return;
						} else if (e instanceof InvocationTargetException) {
							sendGeneratedMessages();
							exception = (Exception)e.getCause();
							break;
						} else {
							continue;
						}
					}
					sendGeneratedMessages();
					currentMessage.resolveContinuations(returnValue);
					return;
				}
			}

			System.err.println("Message processing exception:");
			if (message.getSource() != null) {
				System.err.println("\tSent by: " + message.getSource().toString());
			} else System.err.println("\tSent by: unknown");
			System.err.println("\tReceived by actor: " + toString());
			System.err.println("\tMessage: " + message.toString());
			if (exception == null) {
				if (matches == null) {
					System.err.println("\tNo methods with the same name found.");
					return;
				}
				System.err.println("\tDid not match any of the following: ");
				for (int i = 0; i < matches.length; i++) {
					System.err.print("\t\tMethod: " + matches[i].getName() + "( ");
					Class[] parTypes = matches[i].getParameterTypes();
					for (int j = 0; j < parTypes.length; j++) {
						System.err.print(parTypes[j].getName() + " ");
					}
					System.err.println(")");
				}
			} else {
				System.err.println("\tThrew exception: " + exception);
				exception.printStackTrace();
			}
		}

		HashSet hostList;
		HashSet visited;
		Vector toVisit;
		HashSet toVisitElement;
		WeakReference currentGCAgent = null;
		public void construct(){
			hostList = new HashSet();
			visited = new HashSet();
			toVisit = new Vector();
			toVisitElement = new HashSet();
		}
		public void clearAndSet(String refString) {
//System.out.println("clearAndSet:"+refString);
			hostList.clear();
			visited.clear();
			toVisit.clear();
			toVisitElement.clear();
			if (refString!=null) {
				String location;
				WeakReference ref;
				try {
					if (refString.charAt(0)=='u') {
						location = ServiceFactory.getNaming().queryLocation(new UAN(refString));
						System.out.println(":::::::::::::::::::::::::"+location);
						currentGCAgent = new WeakReference(null, new UAL(location+"salsa/GCAgent"));
					}
					else {
						location = (new UAL(refString)).getLocation();
						currentGCAgent = new WeakReference(null, new UAL(location+"salsa/GCAgent"));
					}
					hostList.add(location);
					{
						Token token_4_0 = new Token();
						// currentGCAgent<-looseFindNonLocal(refString)
						{
							Object _arguments[] = { refString };
							Message message = new Message( self, currentGCAgent, "looseFindNonLocal", _arguments, null, token_4_0 );
							__messages.add( message );
						}
						// ((CycleDetective)self)<-backTrace(token, refString)
						{
							Object _arguments[] = { token_4_0, refString };
							Message message = new Message( self, ((CycleDetective)self), "backTrace", _arguments, token_4_0, currentMessage.getContinuationToken() );
							__messages.add( message );
						}
						throw new CurrentContinuationException();
					}
				}
				catch (Exception e) {
				}

			}
		}
		public WeakReference getRemoteGCAgent(String refString) {
//System.out.println("getRemoteGCAgent:"+refString);
			WeakReference ref;
			String location = "";
			if (refString!=null) {
				try {
					if (refString.charAt(0)=='u') {
						location = ServiceFactory.getNaming().queryLocation(new UAN(refString));
						ref = new WeakReference(null, new UAL(location+"salsa/GCAgent"));
					}
					else {
						location = (new UAL(refString)).getLocation();
						ref = new WeakReference(null, new UAL(location+"salsa/GCAgent"));
					}
					if (!location.equals(currentGCAgent.getUAL().getLocation())) {
						return ref;
					}
				}
				catch (Exception e) {
				}

			}
			return null;
		}
		public void isLocalSnapshotMember() {
//System.out.println("isLocalSnapshotMember");
			if (toVisit.size()==0) {
				System.out.println("do next step!");
				{
					// ((CycleDetective)self)<-synchronizeSnapshot()
					{
						Object _arguments[] = {  };
						Message message = new Message( self, ((CycleDetective)self), "synchronizeSnapshot", _arguments, null, null );
						__messages.add( message );
					}
				}
				return;
			}
			String actor = (String)toVisit.remove(0);
			this.toVisitElement.remove(actor);
			{
				Token token_2_0 = new Token();
				// currentGCAgent<-looseFindNonLocal(actor)
				{
					Object _arguments[] = { actor };
					Message message = new Message( self, currentGCAgent, "looseFindNonLocal", _arguments, null, token_2_0 );
					__messages.add( message );
				}
				// ((CycleDetective)self)<-backTrace(token, actor)
				{
					Object _arguments[] = { token_2_0, actor };
					Message message = new Message( self, ((CycleDetective)self), "backTrace", _arguments, token_2_0, currentMessage.getContinuationToken() );
					__messages.add( message );
				}
				throw new CurrentContinuationException();
			}
		}
		public void backTrace(ActorSnapshot localActor, String id) {
//System.out.println("backTrace:"+id);
			if (localActor==null) {
				WeakReference newGCAgent = getRemoteGCAgent(id);
				if (newGCAgent!=null) {
					hostList.add(newGCAgent.getUAL().getLocation());
					currentGCAgent = newGCAgent;
					{
						Token token_4_0 = new Token();
						// currentGCAgent<-looseFindNonLocal(id)
						{
							Object _arguments[] = { id };
							Message message = new Message( self, currentGCAgent, "looseFindNonLocal", _arguments, null, token_4_0 );
							__messages.add( message );
						}
						// ((CycleDetective)self)<-backTrace(token, id)
						{
							Object _arguments[] = { token_4_0, id };
							Message message = new Message( self, ((CycleDetective)self), "backTrace", _arguments, token_4_0, currentMessage.getContinuationToken() );
							__messages.add( message );
						}
						throw new CurrentContinuationException();
					}
				}
				else {
					System.out.println("failed to search!:");
				}
			}
			else {
				visited.add(id);
				if (localActor.iList!=null) {
					for (int i = 0; i<localActor.iList.length; i++){
						if (!visited.contains((String)localActor.iList[i])&&!toVisitElement.contains((String)localActor.iList[i])) {
							toVisit.add(localActor.iList[i]);
							toVisitElement.add(localActor.iList[i]);
						}
					}
				}
				{
					// ((CycleDetective)self)<-isLocalSnapshotMember()
					{
						Object _arguments[] = {  };
						Message message = new Message( self, ((CycleDetective)self), "isLocalSnapshotMember", _arguments, null, currentMessage.getContinuationToken() );
						__messages.add( message );
					}
					throw new CurrentContinuationException();
				}
			}
		}
		public void synchronizeSnapshot() {
//System.out.println("synchronizeSnapshot");
			Object[] hosts = this.hostList.toArray();
			String myid = null;
			if (getUAN()!=null) {
				myid = getUAN().toString();
			}
			else {
				myid = getUAL().toString();
			}
			if (hosts.length==0) {
				return;
			}
			{
				Token token_2_0 = new Token();
				// join block
				token_2_0.setJoinDirector();
				for (int i = 0; i<hosts.length; i++){
					if (hosts[i]==null) {
continue;					}
					String location = (String)hosts[i];
					WeakReference ref = new WeakReference(null, new UAL(location+"salsa/GCAgent"));
					{
						// ref<-requestSnapShot(myid)
						{
							Object _arguments[] = { myid };
							Message message = new Message( self, ref, "requestSnapShot", _arguments, null, token_2_0 );
							__messages.add( message );
						}
					}
				}
				addJoinToken(token_2_0);
				// reserveSnapshot()
				{
					Object _arguments[] = {  };
					Message message = new Message( self, self, "reserveSnapshot", _arguments, token_2_0, currentMessage.getContinuationToken() );
					__messages.add( message );
				}
				throw new CurrentContinuationException();
			}
		}
		public void reserveSnapshot() {
//System.out.println("reserveSnapshot");
			Object[] hosts = this.hostList.toArray();
			String myid = null;
			if (getUAN()!=null) {
				myid = getUAN().toString();
			}
			else {
				myid = getUAL().toString();
			}
			if (hosts.length==0) {
				return;
			}
			{
				Token token_2_0 = new Token();
				// join block
				token_2_0.setJoinDirector();
				for (int i = 0; i<hosts.length; i++){
					if (hosts[i]==null) {
continue;					}
					String location = (String)hosts[i];
					WeakReference ref = new WeakReference(null, new UAL(location+"salsa/GCAgent"));
					{
						// ref<-reserveSnapshot(myid)
						{
							Object _arguments[] = { myid };
							Message message = new Message( self, ref, "reserveSnapshot", _arguments, null, token_2_0 );
							__messages.add( message );
						}
					}
				}
				addJoinToken(token_2_0);
				// getInvList()
				{
					Object _arguments[] = {  };
					Message message = new Message( self, self, "getInvList", _arguments, token_2_0, currentMessage.getContinuationToken() );
					__messages.add( message );
				}
				throw new CurrentContinuationException();
			}
		}
		public void getInvList() {
//System.out.println("getInvList");
			Object[] actors = this.visited.toArray();
			String myid = null;
			salsa.naming.NamingService naming = ServiceFactory.getNaming();
			if (getUAN()!=null) {
				myid = getUAN().toString();
			}
			else {
				myid = getUAL().toString();
			}
			if (actors.length==0) {
				return;
			}
			{
				Token token_2_0 = new Token();
				Token token_2_1 = new Token();
				// join block
				token_2_0.setJoinDirector();
				for (int i = 0; i<actors.length; i++){
					if (actors[i]==null) {
continue;					}
					String actorLocation = (String)actors[i];
					String host;
					if (actorLocation.charAt(0)=='u') {
						host = naming.queryLocation(new UAN(actorLocation));
					}
					else {
						host = (new UAL(actorLocation)).getLocation();
					}
					WeakReference ref = new WeakReference(null, new UAL(host+"salsa/GCAgent"));
					{
						// ref<-returnInvList(myid, actorLocation)
						{
							Object _arguments[] = { myid, actorLocation };
							Message message = new Message( self, ref, "returnInvList", _arguments, null, token_2_0 );
							__messages.add( message );
						}
					}
				}
				addJoinToken(token_2_0);
				// handleReserveSnapshot(token)
				{
					Object _arguments[] = { token_2_0 };
					Message message = new Message( self, self, "handleReserveSnapshot", _arguments, token_2_0, token_2_1 );
					__messages.add( message );
				}
				// finalizeGC(token)
				{
					Object _arguments[] = { token_2_1 };
					Message message = new Message( self, self, "finalizeGC", _arguments, token_2_1, currentMessage.getContinuationToken() );
					__messages.add( message );
				}
				throw new CurrentContinuationException();
			}
		}
		public Boolean handleReserveSnapshot(Object[] invLists) {
//System.out.println("handleReserveSnapshot");
			for (int i = 0; i<invLists.length; i++){
				if (invLists[i]!=null) {
					Object[] invList = (Object[])invLists[i];
					for (int j = 0; j<invList.length; j++){
						if (invList[j]==null) {
continue;						}
						if (invList[j] instanceof Boolean) {
							return (Boolean)invList[j];
						}
						if (!visited.contains(invList[j])) {
							return new Boolean(false);
						}
					}
				}
			}
			return new Boolean(true);
		}
		public void finalizeGC(Boolean result) {
			System.out.println("finalizeGC:"+result);
			Object[] hosts = this.hostList.toArray();
			String myid = null;
			if (getUAN()!=null) {
				myid = getUAN().toString();
			}
			else {
				myid = getUAL().toString();
			}
			if (hosts.length==0) {
				return;
			}
			try {
				if (result.booleanValue()) {
					for (int i = 0; i<hosts.length; i++){
						if (hosts[i]==null) {
continue;						}
						String location = (String)hosts[i];
						WeakReference ref = new WeakReference(null, new UAL(location+"salsa/GCAgent"));
						{
							// ref<-cycleKilling(myid)
							{
								Object _arguments[] = { myid };
								Message message = new Message( self, ref, "cycleKilling", _arguments, null, null );
								__messages.add( message );
							}
						}
					}
				}
				return;
			}
			catch (Exception e) {
			}

			for (int i = 0; i<hosts.length; i++){
				if (hosts[i]==null) {
continue;				}
				String location = (String)hosts[i];
				WeakReference ref = new WeakReference(null, new UAL(location+"salsa/GCAgent"));
				{
					// ref<-noCycleResume(myid)
					{
						Object _arguments[] = { myid };
						Message message = new Message( self, ref, "noCycleResume", _arguments, null, null );
						__messages.add( message );
					}
				}
			}
		}
		public void act(String[] args) {
			try {
				{
					// ((CycleDetective)self)<-clearAndSet(args[0])
					{
						Object _arguments[] = { args[0] };
						Message message = new Message( self, ((CycleDetective)self), "clearAndSet", _arguments, null, null );
						__messages.add( message );
					}
				}
			}
			catch (Exception e) {
				System.out.println(e);
			}

		}
	}
}