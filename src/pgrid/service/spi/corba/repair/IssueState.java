package pgrid.service.spi.corba.repair;


/**
* pgrid/service/spi/corba/repair/IssueState.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from resources/pgrid_corba.idl
* Thursday, January 19, 2012 9:18:13 PM EET
*/

public class IssueState implements org.omg.CORBA.portable.IDLEntity
{
  private        int __value;
  private static int __size = 3;
  private static pgrid.service.spi.corba.repair.IssueState[] __array = new pgrid.service.spi.corba.repair.IssueState [__size];

  public static final int _PENDING = 0;
  public static final pgrid.service.spi.corba.repair.IssueState PENDING = new pgrid.service.spi.corba.repair.IssueState(_PENDING);
  public static final int _SOLVED = 1;
  public static final pgrid.service.spi.corba.repair.IssueState SOLVED = new pgrid.service.spi.corba.repair.IssueState(_SOLVED);
  public static final int _BROADCASTED = 2;
  public static final pgrid.service.spi.corba.repair.IssueState BROADCASTED = new pgrid.service.spi.corba.repair.IssueState(_BROADCASTED);

  public int value ()
  {
    return __value;
  }

  public static pgrid.service.spi.corba.repair.IssueState from_int (int value)
  {
    if (value >= 0 && value < __size)
      return __array[value];
    else
      throw new org.omg.CORBA.BAD_PARAM ();
  }

  protected IssueState (int value)
  {
    __value = value;
    __array[__value] = this;
  }
} // class IssueState
