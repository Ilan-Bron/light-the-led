'''
@author: ilang
'''


"""
this class will handle the app protocol to save and load the state of usage
"""

import json

path = "users.txt"




def check_user(user_name):
    
    return_value = 0
    
    with open(path , 'r') as f:
        
        users_dict = json.load(f)
        
        if user_name in users_dict:
            return_value =  users_dict[user_name]
        
        else:
            users_dict[user_name] = ""
            return_value = ""
            

    with open(path , 'w') as f:       
        json.dump(users_dict , f)
            
    
    return return_value
        

def save_json(user_name , astr):
    with open(path , 'r') as f:

        #json_str = f.read()
        
        users_dict = json.load(f)
        
        users_dict[user_name] = astr
        
        
    with open(path , 'w') as f:
        json.dump(users_dict , f)
        
        
