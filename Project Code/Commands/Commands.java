public class Commands {

    private String speech;
    private String[][] contacts;

    protected Commands(String speech, String[][] contacts) {
        this.speech = speech;
        this.contacts = contacts;
    }

    private boolean exists(String word, String[][] array) {
        for (int x = 0; x < array.length; x ++) {
            for (int y = 0; y < array[x].length; y ++) {
                if (word.equals(array[x][y])) {
                    return true;
                }
            }
        }
        return false;
    }

    private int search(String word, String[][] array) {
        for (int x = 0; x < array.length; x ++) {
            for (int y = 0; y < array[x].length; y ++) {
                if (word.equals(array[x][y])) {
                    return x;
                }
            }
        }
        return -1;
    }

    protected String[][] getContacts() {
        return contacts;
    }

    //Phone Calls


    //Contacts
    protected String createContact(String name) {
        if (exists(name, contacts)) {
            return "Contact " + name + " already exists";
        } else {
            for (int x = 0; x < contacts.length; x ++) {
                if (contacts[x][0].equals("null")) {
                    contacts[x][0] = name;
                    return "Contact " + name + " has been created";
                }
            }
            return "Error. Please make room for more contacts";
        }
    }

    protected String deleteContact(String name) {
        if (exists(name, contacts)) {
            int row = search(name, contacts);
            for (int x = 0; x < contacts[0].length; x ++) {
                contacts[row][x] = "null";
            }
            return "Contact " + name + " has been deleted";
        } else {
            return name + " is not a contact";
        }
    }

    protected String renameContact(String oldName, String newName) {
        if (exists(oldName, contacts)) {
            contacts[search(oldName, contacts)][0] = newName;
            return oldName + " has been renamed to " + newName;
        } else {
            return "Contact " + oldName + " is not a contact";
        }
    }

    protected String getHome(String name) {
        if (exists(name, contacts)) {
            if (contacts[search(name, contacts)][1] == null) {
                return "The home phone number is not set for " + name;
            } else {
                return "The home phone number of " + name + " is " + contacts[search(name, contacts)][1];
            }
        } else {
            return name + " is not a contact";
        }
    }

    protected String setHome(String name, String number) {
        if (exists(name, contacts)) {
            contacts[search(name, contacts)][1] = number;
            return "The home phone number of " + name + " has been set to " + number;
        } else {
            return name + " is not a contact";
        }
    }

    protected String getMobile(String name) {
        if (exists(name, contacts)) {
            if (contacts[search(name, contacts)][2] == null) {
                return "The mobile phone number is not set for " + name;
            } else {
                return "The mobile phone number of " + name + " is " + contacts[search(name, contacts)][2];
            }
        } else {
            return name + " is not a contact";
        }
    }

    protected String setMobile(String name, String number) {
        if (exists(name, contacts)) {
            contacts[search(name, contacts)][2] = number;
            return "The mobile phone number of " + name + " has been set to " + number;
        } else {
            return name + " is not a contact";
        }
    }

    protected String getWork(String name) {
        if (exists(name, contacts)) {
            if (contacts[search(name, contacts)][3] == null) {
                return "The work phone number is not set for " + name;
            } else {
                return "The work phone number of " + name + " is " + contacts[search(name, contacts)][3];
            }
        } else {
            return name + " is not a contact";
        }
    }

    protected String setWork(String name, String number) {
        if (exists(name, contacts)) {
            contacts[search(name, contacts)][3] = number;
            return "The work phone number of " + name + " has been set to " + number;
        } else {
            return name + " is not a contact";
        }
    }

    protected String getAge(String name) {
        if (exists(name, contacts)) {
            if (contacts[search(name, contacts)][4] == null) {
                return "Age is not set for " + name;
            } else {
                return name + " is " + contacts[search(name, contacts)][4] + " years old";
            }
        } else {
            return name + " is not a contact";
        }
    }

    protected String setAge(String name, String age) {
        if (exists(name, contacts)) {
            contacts[search(name, contacts)][4] = age;
            return "The age of " + name + " has been set to " + age;
        } else {
            return name + " is not a contact";
        }
    }

    protected String getBirthday(String name) {
        if (exists(name, contacts)) {
            if (contacts[search(name, contacts)][5] == null) {
                return "Birthday is not set for " + name;
            } else {
                return name + " was born on " + contacts[search(name, contacts)][5];
            }
        } else {
            return name + " is not a contact";
        }
    }

    protected String setBirthday(String name, String birthday) {
        if (exists(name, contacts)) {
            contacts[search(name, contacts)][5] = birthday;
            return "The birthday of " + name + " has been set to " + birthday;
        } else {
            return name + " is not a contact";
        }
    }

    protected String getAddress(String name) {
        if (exists(name, contacts)) {
            if (contacts[search(name, contacts)][6] == null) {
                return "Address is not set for " + name;
            } else {
                return name + " lives on " + contacts[search(name, contacts)][6];
            }
        } else {
            return name + " is not a contact";
        }
    }

    protected String setAddress(String name, String address) {
        if (exists(name, contacts)) {
            contacts[search(name, contacts)][6] = address;
            return "The address of " + name + " has been set to " + address;
        } else {
            return name + " is not a contact";
        }
    }

    protected String getTag(String name) {
        if (exists(name, contacts)) {
            if (contacts[search(name, contacts)][7] == null) {
                return "Tag is not set for " + name;
            } else {
                return name + " is a " + contacts[search(name, contacts)][7];
            }
        } else {
            return name + " is not a contact";
        }
    }

    protected String setTag(String name, String tag) {
        if (exists(name, contacts)) {
            contacts[search(name, contacts)][7] = tag;
            return "The age of " + name + " has been set to " + tag;
        } else {
            return name + " is not a contact";
        }
    }
}
