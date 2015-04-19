import pdb
import os

class Training:
    def __init__(self, fnm):
        self.fnm = fnm
        #self.d = {"<routenm>": ["cats","g p e","golden panther express","panther express"],\
        #          "<platform>":["m m c","m m campus","main campus","engineer center","e c","biscayne bay campus","b b c","g c","graham center","p g five","p g five east","one o nine tower","lot five","lot three","biscayne boulevard"],\
        #          "<bus>":["bus","shuttle","buses","shuttles"],\
        #          "<location>":["location","position"],\
        #          "<now>":["now","currently"],\
        #          "<eta>":["estimate arrival time","e t a","arrival time"],\
        #          "<station>":["station","stop","stations","stops"],\
        #          "<nearest>":["nearest","closest"],\
        #          "<route>":["route","path"],\
        #          "<garage>":["p g one","gold","p g two","blue","p g three","panther","p g four","red","p g five","market station","p g six","tech station"],\
        #          "<grage1>":["garage","spot","garages","spots","parking garage","parking spots"],\
        #          "<parkingposition>":["space","spot","lot","garage","place"]}

        self.d = { "<bus>":["bus","shuttle","buses","shuttles"],\
                  "<location>":["location","position"],\
                  "<now>":["now","currently"],\
                  "<eta>":["estimate arrival time","e t a","arrival time"],\
                  "<station>":["station","stop","stations","stops"],\
                  "<nearest>":["nearest","closest"],\
                  "<route>":["route","path"],\
                  "<grage1>":["garage","spot","garages","spots","parking garage","parking spots"],\
                  "<parkingposition>":["space","spot","lot","garage","place"]}

        self.pattern = {"<parkingposition>":["space","spot","lot","garage","place"],\
                        "<parkinggarage>":["parking garage <garage>","garage <garage>"],\
                        "<routebusstation>":["<bus> <station>","<routenm> <bus> <station>","<bus> <station> <platform>","<bus> <station> for <routenm>","<bus> <station> to <platform>"],\
                        "<buspattern>":["<bus> <location>","<routenm> <bus>","<bus> <location> from <platform>","<bus> <location> to <platform>","<routenm> <bus> <location>"],\
                        "<busroute>":["<routenm> <bus> <route>","<routenm> <route>","<route> from <platform>","<route> to <platform>"]}
        self.words = {} 
        self.cluster = {}
        self.idx = 1

    def open_pattern(self):
        #self.generate_word()
        #pdb.set_trace()
        f = open(self.fnm)
        lines = f.readlines()
        f.close()

        f = open(self.fnm+"open_pattern", 'w')
        for cid, line in enumerate(lines):
            attr = line.strip().split(",")
            lable = attr[0]
            f.write("%s" %(lable))
            for sentence in attr[1:]: 
                rst = [[]]
                words = sentence.strip().split(" ")
                for word in words: 
                    if self.pattern.has_key(word):
                        new_rst = []
                        subsititue = self.pattern[word]
                        for sub in subsititue:  
                            #self.words.setdefault(sub, idx)
                            for subrst in rst:
                                subrst.append(sub)
                                new_rst.append(subrst[:])
                                subrst.pop()
                        rst = new_rst
                            #rst[self.words[sub]-1] = '1'
                    else: 
                        for subrst in rst:
                            subrst.append(word)

                for srst in rst:
                    f.write(",%s" %(' '.join(srst)))
            f.write("\n")
        f.close()
        self.fnm = self.fnm+"open_pattern"
        self.test_data_f()

    def generate_word(self):
        f = open(self.fnm)
        lines = f.readlines()
        f.close()
        for cid, line in enumerate(lines):
            attr = line.strip().split(",")
            lable = attr[0]
            self.cluster[lable] = cid
            for sentence in attr[1:]: 
                #rst = []
                words = sentence.strip().split(" ")
                for word in words: 
                    if self.d.has_key(word):
                        subsititue = self.d[word]
                        for subrst in subsititue:  
                            subattr = subrst.split(" ")
                            for sub in subattr:
                                if not self.words.has_key(sub):
                                    self.words[sub] = self.idx
                                    self.idx += 1
                    else: 
                        if not self.words.has_key(word):
                            self.words[word] = self.idx
                            self.idx += 1
                        #rst.append("%d:1" %self.words[word])
                #print "%d %s" %(cid, ' '.join(rst))
        #pdb.set_trace()
        print self.words

    def geberate_random_f(self):
        self.generate_word()
        #pdb.set_trace()
        print ','.join(self.words.keys())
        f = open(self.fnm)
        lines = f.readlines()
        f.close()
        for cid, line in enumerate(lines):
            attr = line.strip().split(",")
            lable = attr[0]
            for sentence in attr[1:]: 
                rst = ['0' for i in range(len(self.words))]
                words = sentence.strip().split(" ")
                for word in words: 
                    if self.d.has_key(word):
                        subsititue = self.d[word]
                        for sub in subsititue:  
                            #self.words.setdefault(sub, idx)
                            if not self.words.has_key(sub):
                                self.words[sub] = self.idx
                                self.idx += 1
                            rst[self.words[sub]-1] = '1'
                    else: 
                        if not self.words.has_key(word):
                            self.words[word] = self.idx
                            self.idx += 1
                        rst[self.words[word]-1] = '1'
                print "%s,%s" %(' '.join(rst), lable)

    def test_data_f(self):
        self.generate_word()
        #pdb.set_trace()
        import operator
        sorted_x = sorted(self.words.items(), key=operator.itemgetter(1))
        print ','.join([k for k, v in sorted_x])
        f = open(self.fnm)
        lines = f.readlines()
        f.close()
        for cid, line in enumerate(lines):
            attr = line.strip().split(",")
            lable = attr[0]
            for sentence in attr[1:]: 
                rst = [[]]
                words = sentence.strip().split(" ")
                for word in words: 
                    if self.d.has_key(word):
                        new_rst = []
                        subsititue = self.d[word]
                        for sub in subsititue:  
                            #self.words.setdefault(sub, idx)
                            if not self.words.has_key(sub):
                                self.words[sub] = self.idx
                                self.idx += 1
                            for subrst in rst:
                                subrst.append(self.words[sub]-1)
                                new_rst.append(subrst[:])
                                subrst.pop()
                        rst = new_rst
                            #rst[self.words[sub]-1] = '1'
                    else: 
                        if not self.words.has_key(word):
                            self.words[word] = self.idx
                            self.idx += 1
                        for subrst in rst:
                            subrst.append(self.words[word]-1)

                for srst in rst:
                    data = ['0' for i in range(len(self.words))] 
                    #print sentence
                    #pdb.set_trace()
                    for idx in srst:
                        data[idx] = '1' 
                    print "%s,%s" %(','.join(data), lable)

    def train_data_svm(self):
        self.generate_word()
        #pdb.set_trace()
        print ','.join(self.words.keys())
        f = open(self.fnm)
        lines = f.readlines()
        f.close()
        for cid, line in enumerate(lines):
            attr = line.strip().split(",")
            lable = attr[0]
            for sentence in attr[1:]: 
                rst = [[]]
                words = sentence.strip().split(" ")
                for word in words: 
                    if self.d.has_key(word):
                        new_rst = []
                        subsititue = self.d[word]
                        for sub in subsititue:  
                            #self.words.setdefault(sub, idx)
                            if not self.words.has_key(sub):
                                self.words[sub] = self.idx
                                self.idx += 1
                            for subrst in rst:
                                subrst.append("%d:1" %self.words[sub])
                                new_rst.append(subrst[:])
                                subrst.pop()
                        rst = new_rst
                            #rst[self.words[sub]-1] = '1'
                    else: 
                        if not self.words.has_key(word):
                            self.words[word] = self.idx
                            self.idx += 1
                        for subrst in rst:
                            subrst.append("%d:1" %self.words[word])

                for srst in rst:
                    print "%d %s" %(cid, ' '.join(srst))

    def test_data_svm(self, testnm):
        self.generate_word()
        #pdb.set_trace()
        f = open(testnm)
        lines = f.readlines()
        f.close()
        print self.cluster
        for cid, line in enumerate(lines):
            attr = line.strip().split(",")
            lable = attr[0]
            cid = self.cluster[lable]
            for sentence in attr[1:]: 
                rst = [[]]
                words = sentence.strip().split(" ")
                for word in words: 
                    if self.d.has_key(word):
                        new_rst = []
                        subsititue = self.d[word]
                        for sub in subsititue:  
                            #self.words.setdefault(sub, idx)
                            if not self.words.has_key(sub):
                                self.words[sub] = self.idx
                                self.idx += 1
                            for subrst in rst:
                                subrst.append("%d:1" %self.words[sub])
                                new_rst.append(subrst[:])
                                subrst.pop()
                        rst = new_rst
                            #rst[self.words[sub]-1] = '1'
                    else: 
                        if not self.words.has_key(word):
                            self.words[word] = self.idx
                            self.idx += 1
                        for subrst in rst:
                            subrst.append("%d:1" %self.words[word])

                for srst in rst:
                    #data = ['0' for i in range(len(self.words))] 
                    #for idx in srst:
                    #    data[idx] = '1' 
                    #pdb.set_trace()
                    print "%d %s" %(cid, ' '.join(srst))


    def train_word_data_svm(self):

        self.generate_word()
        #pdb.set_trace()
        print ','.join(self.words.keys())
        f = open(self.fnm)
        lines = f.readlines()
        f.close()
        for cid, line in enumerate(lines):
            attr = line.strip().split(",")
            lable = attr[0]
            for sentence in attr[1:]: 
                rst = [[]]
                rst_words = [[]]
                words = sentence.strip().split(" ")
                for word in words: 
                    if self.d.has_key(word):
                        subsititue = self.d[word]
                        new_rst = []
                        new_rst_word = []
                        for subrst in subsititue:  
                            #self.words.setdefault(sub, idx)
                            subattr = subrst.split(" ")
                            for sub in subattr:
                                #print word, subattr, rst
                                for subrst in rst:
                                    subrst.append("%d:1" %self.words[sub])
                                for subrst_word in rst_words:
                                    subrst_word.append("%s" %sub)

                            for idx, subrst in enumerate(rst):
                                new_rst.append(subrst[:])
                                subrst_word = rst_words[idx]
                                new_rst_word.append(subrst_word[:])
                                for j in range(len(subattr)):
                                    subrst.pop()
                                    subrst_word.pop()

                        rst = new_rst
                        rst_words = new_rst_word
                            #rst[self.words[sub]-1] = '1'
                    else: 
                        for subrst in rst:
                            subrst.append("%d:1" %self.words[word])

                        for subrst_word in rst_words:
                            subrst_word.append("%s" %word)

                for idx, srst in enumerate(rst):
                    #print "%d %s" %(cid, ' '.join(srst))
                    print "%s %s" %(lable, ' '.join(rst_words[idx]))

    def train_word_data_dt_rf(self):

        self.generate_word()
        #pdb.set_trace()
        import operator
        sorted_x = sorted(self.words.items(), key=operator.itemgetter(1))
        print ','.join([items[0] for items in sorted_x ])
        f = open(self.fnm)
        lines = f.readlines()
        f.close()
        for cid, line in enumerate(lines):
            attr = line.strip().split(",")
            lable = attr[0]
            for sentence in attr[1:]: 
                rst = [[]]
                rst_words = [[]]
                words = sentence.strip().split(" ")
                for word in words: 
                    if self.d.has_key(word):
                        subsititue = self.d[word]
                        new_rst = []
                        new_rst_word = []
                        for subrst in subsititue:  
                            #self.words.setdefault(sub, idx)
                            subattr = subrst.split(" ")
                            for sub in subattr:
                                #print word, subattr, rst
                                for subrst in rst:
                                    subrst.append("%d:1" %self.words[sub])
                                for subrst_word in rst_words:
                                    subrst_word.append("%s" %sub)

                            for idx, subrst in enumerate(rst):
                                new_rst.append(subrst[:])
                                subrst_word = rst_words[idx]
                                new_rst_word.append(subrst_word[:])
                                for j in range(len(subattr)):
                                    subrst.pop()
                                    subrst_word.pop()

                        rst = new_rst
                        rst_words = new_rst_word
                            #rst[self.words[sub]-1] = '1'
                    else: 
                        for subrst in rst:
                            subrst.append("%d:1" %self.words[word])

                        for subrst_word in rst_words:
                            subrst_word.append("%s" %word)

                for idx, srst in enumerate(rst):
                    #print "%d %s" %(cid, ' '.join(srst))
                    #pdb.set_trace()
                    idx_val = [int(x.split(":")[0])  for x in srst] 
                    f_rst = ['0' for x in range(len(self.words))]
                    for xx in idx_val:           
                        f_rst[xx-1] = '1'
                    print "%s,%s" %(','.join(f_rst), lable)


    def generate(self):
        f = open(self.fnm)
        lines = f.readlines()
        f.close()
        for cid, line in enumerate(lines):
            attr = line.strip().split(",")
            lable = attr[0]
            for sentence in attr[1:]: 
                rst = []
                words = sentence.strip().split(" ")
                for word in words: 
                    if self.d.has_key(word):
                        subsititue = self.d[word]
                        for sub in subsititue:  
                            #self.words.setdefault(sub, idx)
                            if not self.words.has_key(sub):
                                self.words[sub] = self.idx
                                self.idx += 1
                            rst.append("%d:1" %self.words[sub])
                    else: 
                        if not self.words.has_key(word):
                            self.words[word] = self.idx
                            self.idx += 1
                        rst.append("%d:1" %self.words[word])
                print "%d %s" %(cid, ' '.join(rst))
        #pdb.set_trace()
        #print self.words

if __name__ == "__main__":
    #tr = Training("data/traindata1.txt")
    #tr = Training("data/traindata3.txt")

    #tr = Training("data/traindata2.txt")
    #tr.generate()
    #tr.geberate_random_f()
    #tr.test_data_f()
    tr = Training("data/traindata3.txtopen_pattern")
    #tr.open_pattern()
    #tr.train_word_data_svm()
    tr.train_word_data_dt_rf()
    #tr.test_data_svm("data/test1.txt")
    #tr.test_word_data_svm(self, testnm):
